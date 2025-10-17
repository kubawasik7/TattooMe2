package TattooMe.TattooMe.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "Nazwa uzytkownika jest wymagana")
    @Size(min = 3, max = 30, message = "Nazwa uzytkownika musi mieć od 3 do 25 znakow")
    private String nickname;

    @NotBlank(message = "Haslo jest wymagane")
    private String password;
}
