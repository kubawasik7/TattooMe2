package TattooMe.TattooMe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class UserDTO {
    private UUID id;
    private String nickname;
    private String name;
    private String surname;
    private String email;
    private String description;
    private String base64Image;
}
