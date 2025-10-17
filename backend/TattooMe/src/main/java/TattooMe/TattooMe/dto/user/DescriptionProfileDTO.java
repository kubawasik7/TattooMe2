package TattooMe.TattooMe.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DescriptionProfileDTO {
    @NotBlank(message = "Opis nie może być pusty")
    @Size(max = 2000, message = "Opis nie może mieć więcej niż 2000 znaków")
    private String description;
}
