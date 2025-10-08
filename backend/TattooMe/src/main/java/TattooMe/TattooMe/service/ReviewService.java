package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.ReviewAnswerDTO;
import TattooMe.TattooMe.dto.ReviewDTO;
import TattooMe.TattooMe.entity.*;
import TattooMe.TattooMe.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewAnswerRepository reviewAnswerRepository;
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private UserRepository userRepository;

    public List<ReviewDTO> getReviewsForArtist(UUID artistId) {
        return reviewRepository.findByTarget_Id(artistId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsForStudio(UUID studioId) {
        return reviewRepository.findByTattooStudio_Id(studioId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Optional<ReviewDTO> getReviewForVisit(UUID visitId) {
        return reviewRepository.findByVisit_Id(visitId)
                .map(this::toDto);
    }

    public ReviewDTO addReview(UUID visitId, int rate, String content, UUID authorId) throws AccessDeniedException {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Wizyta nie znaleziona"));

        if (!visit.getClient().getId().equals(authorId)) {
            throw new AccessDeniedException("Tylko klient moze wystawic opinie");
        }

        if (!"ZATWIERDZONA".equals(visit.getStatus().getName())
                || visit.getArtistDate().getDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Opinia moze byc wystawiona tylko po zakonczonej wizycie");
        }

        if (reviewRepository.findByVisit_Id(visitId).isPresent()) {
            throw new RuntimeException("Opinia juz istnieje dla tej wizyty");
        }

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Uzytkownik nie znaleziony"));

        Review review = new Review();
        review.setRate(rate);
        review.setContent(content);
        review.setAuthor(author);
        review.setVisit(visit);
        review.setTarget(visit.getArtist());
        review.setTattooStudio(visit.getTattooStudio());
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);
        return toDto(review);
    }

    public ReviewAnswerDTO addAnswer(UUID reviewId, String content, UUID userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Wizyta nie znaleziona"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Uzytkownik nie znaleziony"));

        ReviewAnswer answer = new ReviewAnswer();
        answer.setContent(content);
        answer.setUser(user);
        answer.setReview(review);
        answer.setCreatedAt(LocalDateTime.now());

        if (review.getTattooStudio() != null) {
            answer.setTattooStudio(review.getTattooStudio());
        } else {
            answer.setArtist(review.getTarget());
        }

        reviewAnswerRepository.save(answer);
        return new ReviewAnswerDTO(answer.getId(), answer.getContent(), answer.getCreatedAt(), user.getNickname());
    }


    private ReviewDTO toDto(Review review) {
        List<ReviewAnswerDTO> answers = review.getAnswers().stream()
                .map(a -> new ReviewAnswerDTO(a.getId(), a.getContent(), a.getCreatedAt(), a.getUser().getNickname()))
                .collect(Collectors.toList());

        return new ReviewDTO(
                review.getId(),
                review.getRate(),
                review.getContent(),
                review.getCreatedAt(),
                review.getAuthor().getNickname(),
                review.getTarget() != null ? review.getTarget().getId() : null,
                review.getTattooStudio() != null ? review.getTattooStudio().getId() : null,
                answers
        );
    }
}

