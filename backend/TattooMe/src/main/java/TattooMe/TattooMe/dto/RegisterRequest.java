package TattooMe.TattooMe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "Nickname nie może być pusty")
    @Size(min = 3, max = 25, message = "Nickname musi mieć od 3 do 25 znaków")
    private String nickname;

    @NotBlank(message = "Hasło nie może być puste")
    @Size(min = 6, max = 60, message = "Hasło musi mieć co najmniej 6 znaków")
    private String password;

    @NotBlank(message = "Email nie może być pusty")
    @Email(message = "Niepoprawny format adresu email")
    private String email;
    private String role;
}
