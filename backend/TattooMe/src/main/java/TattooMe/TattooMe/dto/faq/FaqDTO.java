package TattooMe.TattooMe.dto.faq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FaqDTO {
    private UUID id;

    @NotBlank(message = "Pytanie nie może być puste")
    @Size(max = 150, message = "Pytanie nie może mieć więcej niż 150 znaków")
    private String question;

    @NotBlank(message = "Odpowiedź nie może być pusta")
    @Size(max = 1000, message = "Odpowiedź nie może mieć więcej niż 1000 znaków")
    private String answer;
}
