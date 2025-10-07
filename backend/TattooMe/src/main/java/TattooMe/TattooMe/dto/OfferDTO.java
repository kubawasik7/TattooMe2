package TattooMe.TattooMe.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OfferDTO {
    private UUID id;

    @NotNull(message = "Data rozpoczęcia jest wymagana")
    @FutureOrPresent(message = "Data rozpoczęcia nie może być w przeszłości")
    private LocalDateTime startDate;

    @NotNull(message = "Data zakończenia jest wymagana")
    @Future(message = "Data zakończenia musi być w przyszłości")
    private LocalDateTime endDate;

    @NotBlank(message = "Opis jest wymagany")
    @Size(max = 500, message = "Opis nie może przekraczać 500 znaków")
    private String description;
}
