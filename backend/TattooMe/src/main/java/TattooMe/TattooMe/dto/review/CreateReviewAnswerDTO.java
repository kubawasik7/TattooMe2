package TattooMe.TattooMe.dto.review;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateReviewAnswerDTO {
    private UUID reviewId;
    private String content;
}