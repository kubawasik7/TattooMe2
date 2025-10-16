package TattooMe.TattooMe.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateOfferDTO {
    @NotNull(message = "Data rozpoczęcia jest wymagana")
    @Future(message = "Data rozpoczęcia musi być w przyszłości")
    private LocalDateTime startDate;

    @NotNull(message = "Data zakończenia jest wymagana")
    @Future(message = "Data zakończenia musi być w przyszlosci")
    private LocalDateTime endDate;

    @NotBlank(message = "Opis jest wymagany")
    @Size(min = 5, max = 500, message = "Opis musi mieć od 5 do 500 znakow")
    private String description;
}
