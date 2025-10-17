package TattooMe.TattooMe.dto.register;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterResponse {
    private String nickname;
    private String email;
    private String role;
}
