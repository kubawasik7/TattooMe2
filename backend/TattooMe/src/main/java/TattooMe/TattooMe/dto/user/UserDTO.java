package TattooMe.TattooMe.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private UUID id;
    private String nickname;
    private String name;
    private String surname;
    private String email;
    private String description;
    private byte[] profilePicture;
    private Double averageRate;
    private Long reviewsCount;
}
