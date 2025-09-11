package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.ReviewAnswerDTO;
import TattooMe.TattooMe.dto.ReviewDTO;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{visitId}")
    public ResponseEntity<ReviewDTO> addReview(
            @PathVariable UUID visitId,
            @RequestParam int rate,
            @RequestParam(required = false) String content,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(reviewService.addReview(visitId, rate, content, user.getId()));
    }

    @PostMapping("/{reviewId}/answers")
    public ResponseEntity<ReviewAnswerDTO> addAnswer(
            @PathVariable UUID reviewId,
            @RequestParam String content,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        return ResponseEntity.ok(
                reviewService.addAnswer(reviewId, content, userDetails.getId())
        );
    }
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsForArtist(@PathVariable UUID artistId) {
        return ResponseEntity.ok(reviewService.getReviewsForArtist(artistId));
    }

    @GetMapping("/studio/{studioId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsForStudio(@PathVariable UUID studioId) {
        return ResponseEntity.ok(reviewService.getReviewsForStudio(studioId));
    }
    @GetMapping("/visit/{visitId}")
    public ResponseEntity<ReviewDTO> getReviewForVisit(@PathVariable UUID visitId) {
        return reviewService.getReviewForVisit(visitId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
