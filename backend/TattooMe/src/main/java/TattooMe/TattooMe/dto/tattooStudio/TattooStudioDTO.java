package TattooMe.TattooMe.dto.tattooStudio;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TattooStudioDTO {
    private UUID id;
    private String name;
    private String city;
    private String ownerNickname;
}
