package TattooMe.TattooMe.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateReviewDTO {

    @NotNull(message = "ID wizyty jest wymagane")
    private UUID visitId;

    @Min(value = 1, message = "Ocena musi być co najmniej 1")
    @Max(value = 5, message = "Ocena nie może być większa niż 5")
    private int rate;

    @NotBlank(message = "Treść opinii nie może być pusta")
    private String content;
}