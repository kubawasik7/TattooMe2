package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.review.CreateReviewAnswerDTO;
import TattooMe.TattooMe.dto.review.CreateReviewDTO;
import TattooMe.TattooMe.dto.review.ReviewAnswerDTO;
import TattooMe.TattooMe.dto.review.ReviewDTO;
import TattooMe.TattooMe.entity.*;
import TattooMe.TattooMe.mapper.ReviewAnswerMapper;
import TattooMe.TattooMe.mapper.ReviewMapper;
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
    @Autowired
    private ReviewMapper reviewMapper;
    @Autowired
    private ReviewAnswerMapper reviewAnswerMapper;

    public List<ReviewDTO> getReviewsForArtist(UUID artistId) {
        return reviewRepository.findByTarget_Id(artistId).stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    public List<ReviewDTO> getReviewsForStudio(UUID studioId) {
        return reviewRepository.findByTattooStudio_Id(studioId).stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    public Optional<ReviewDTO> getReviewForVisit(UUID visitId) {
        return reviewRepository.findByVisit_Id(visitId)
                .map(reviewMapper::toDTO);
    }

    public ReviewDTO addReview(CreateReviewDTO dto, UUID authorId) throws AccessDeniedException {
        Visit visit = visitRepository.findById(dto.getVisitId())
                .orElseThrow(() -> new EntityNotFoundException("Wizyta nie znaleziona"));

        if (!visit.getClient().getId().equals(authorId)) {
            throw new AccessDeniedException("Tylko klient może wystawić opinię");
        }

        if (!"ZATWIERDZONA".equals(visit.getStatus().getName())
                || visit.getArtistDate().getDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Opinia może być wystawiona tylko po zakończonej wizycie");
        }

        if (reviewRepository.findByVisit_Id(dto.getVisitId()).isPresent()) {
            throw new RuntimeException("Opinia już istnieje dla tej wizyty");
        }

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony"));

        Review review = reviewMapper.fromCreateDTO(dto);
        review.setAuthor(author);
        review.setVisit(visit);
        review.setTarget(visit.getArtist());
        review.setTattooStudio(visit.getTattooStudio());
        review.setCreatedAt(LocalDateTime.now());

        reviewRepository.save(review);
        return reviewMapper.toDTO(review);
    }

    public ReviewAnswerDTO addAnswer(CreateReviewAnswerDTO dto, UUID userId) {
        Review review = reviewRepository.findById(dto.getReviewId())
                .orElseThrow(() -> new EntityNotFoundException("Opinia nie znaleziona"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony"));

        boolean canAnswer = (review.getTarget() != null && review.getTarget().getId().equals(userId)) ||
                (review.getTattooStudio() != null &&
                        review.getTattooStudio().getOwner().getId().equals(userId));

        if (!canAnswer) {
            throw new RuntimeException("Nie masz uprawnień do odpowiedzi na ta opinie");
        }

        ReviewAnswer answer = reviewAnswerMapper.fromCreateDTO(dto);
        answer.setUser(user);
        answer.setReview(review);
        answer.setCreatedAt(LocalDateTime.now());

        if (review.getTattooStudio() != null) {
            answer.setTattooStudio(review.getTattooStudio());
        } else {
            answer.setArtist(review.getTarget());
        }

        reviewAnswerRepository.save(answer);
        return reviewAnswerMapper.toDTO(answer);
    }
}

