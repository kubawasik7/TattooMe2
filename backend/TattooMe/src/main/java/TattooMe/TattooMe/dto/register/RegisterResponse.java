package TattooMe.TattooMe.dto.register;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {
    private String nickname;
    private String email;
    private String role;
}
