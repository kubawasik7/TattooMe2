package TattooMe.TattooMe.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String nickname;
    private String password;
    private String email;
    private String role;
}
