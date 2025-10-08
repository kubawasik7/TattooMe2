package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.ReviewAnswerDTO;
import TattooMe.TattooMe.dto.ReviewDTO;
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
    public ResponseEntity<ReviewDTO> addReview(@PathVariable UUID visitId,
                                               @RequestParam int rate,
                                               @RequestParam(required = false) String content,
                                               @AuthenticationPrincipal CustomUserDetails user) throws AccessDeniedException {
        ReviewDTO review = reviewService.addReview(visitId, rate, content, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PostMapping("/{reviewId}/answers")
    public ResponseEntity<ReviewAnswerDTO> addAnswer(@PathVariable UUID reviewId,
                                                     @RequestParam String content,
                                                     @AuthenticationPrincipal CustomUserDetails userDetails) {
        ReviewAnswerDTO reviewAnswer = reviewService.addAnswer(reviewId, content, userDetails.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewAnswer);
    }
}
