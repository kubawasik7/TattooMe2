package TattooMe.TattooMe.dto;

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
