package TattooMe.TattooMe.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateDTO {
    @NotBlank(message = "Nickname nie może być pusty")
    @Size(min = 3, max = 25, message = "Nickname musi mieć od 3 do 25 znaków")
    private String nickname;

    @NotBlank(message = "Imię nie może być puste")
    @Size(min = 2, max = 50, message = "Imię musi mieć od 2 do 50 znaków")
    private String name;

    @NotBlank(message = "Nazwisko nie może być puste")
    @Size(min = 2, max = 50, message = "Nazwisko musi mieć od 2 do 50 znaków")
    private String surname;

    @NotBlank(message = "Email nie może być pusty")
    @Email(message = "Nieprawidłowy format adresu email")
    @Size(max = 100, message = "Email może mieć maksymalnie 100 znaków")
    private String email;
}