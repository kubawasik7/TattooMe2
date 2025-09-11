package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.ReviewAnswerDTO;
import TattooMe.TattooMe.dto.ReviewDTO;
import TattooMe.TattooMe.entity.*;
import TattooMe.TattooMe.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewAnswerRepository reviewAnswerRepository;
    private final VisitRepository visitRepository;
    private final UserRepository userRepository;
    private final TattooStudioRepository tattooStudioRepository;

    public ReviewDTO addReview(UUID visitId, int rate, String content, UUID authorId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        if (!visit.getClient().getId().equals(authorId)) {
            throw new RuntimeException("Only the client can review this visit");
        }

        if (!"ZATWIERDZONA".equals(visit.getStatus().getName())
                || visit.getArtistDate().getDate().isAfter(LocalDateTime.now())) {
            throw new RuntimeException("Review can only be added after finished visit");
        }

        if (reviewRepository.findByVisit_Id(visitId).isPresent()) {
            throw new RuntimeException("Review already exists for this visit");
        }

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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
                .orElseThrow(() -> new RuntimeException("Review not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

