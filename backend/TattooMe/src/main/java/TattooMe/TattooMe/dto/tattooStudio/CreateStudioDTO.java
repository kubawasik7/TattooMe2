package TattooMe.TattooMe.dto.tattooStudio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStudioDTO {
    @NotBlank(message = "Nazwa studia nie może być pusta")
    @Size(min = 3, max = 50, message = "Nazwa studia musi miec miedzy 3 a 50 znakow")
    private String name;

    @NotBlank(message = "Miasto nie może być puste")
    @Size(min = 3, max = 40, message = "Miasto musi miec miedzy 3 a 40 znakow")
    private String city;

    @NotBlank(message = "Ulica nie może być pusta")
    @Size(min = 3, max = 35, message = "Ulica musi miec miedzy 3 a 35 znakow")
    private String street;

    @NotBlank(message = "Numer ulicy nie może być pusty")
    @Size(min = 1, max = 10, message = "Numer ulicy musi miec miedzy 1 a 10 znakow")
    private String streetNumber;

    @NotBlank(message = "Kod pocztowy nie może być pusty")
    @Pattern(regexp = "\\d{2}-\\d{3}", message = "Kod pocztowy musi mieć format XX-XXX")
    private String postalCode;
}
