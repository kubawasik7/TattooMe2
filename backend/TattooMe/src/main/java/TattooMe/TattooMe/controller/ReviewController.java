package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.review.CreateReviewAnswerDTO;
import TattooMe.TattooMe.dto.review.CreateReviewDTO;
import TattooMe.TattooMe.dto.review.ReviewAnswerDTO;
import TattooMe.TattooMe.dto.review.ReviewDTO;
import TattooMe.TattooMe.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsForArtist(@PathVariable UUID artistId) {
        return ResponseEntity.ok(reviewService.getReviewsForArtist(artistId));
    }

    @GetMapping("/studio/{studioId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsForStudio(@PathVariable UUID studioId) {
        return ResponseEntity.ok(reviewService.getReviewsForStudio(studioId));
    }

    @GetMapping("/visit/{visitId}")
    public ResponseEntity<Optional<ReviewDTO>> getReviewForVisit(@PathVariable UUID visitId) {
        return ResponseEntity.ok(reviewService.getReviewForVisit(visitId));
    }

    @PostMapping("/{visitId}")
    public ResponseEntity<ReviewDTO> addReview(@RequestBody CreateReviewDTO dto,
                                               @AuthenticationPrincipal CustomUserDetails user) throws AccessDeniedException {
        ReviewDTO review = reviewService.addReview(dto, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PostMapping("/answers")
    public ResponseEntity<ReviewAnswerDTO> addAnswer(@RequestBody CreateReviewAnswerDTO dto,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        ReviewAnswerDTO reviewAnswer = reviewService.addAnswer(dto, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewAnswer);
    }
}
