package TattooMe.TattooMe.dto.review;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateReviewAnswerDTO {
    private UUID reviewId;

    @NotBlank(message = "Treść opinii nie może być pusta")
    @Size(max = 255, message = "Treść opinii nie może przekraczać 255 znaków")
    private String content;
}