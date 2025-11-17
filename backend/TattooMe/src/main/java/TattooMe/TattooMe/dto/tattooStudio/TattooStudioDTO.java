package TattooMe.TattooMe.dto.tattooStudio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor

public class TattooStudioDTO {

    private UUID id;
    private String name;
    private String city;
    private String street;
    private String streetNumber;
    private String postalCode;
    private String description;
    private byte[] profilePicture;
    private String ownerNickname;
    private Double averageRate;
    private Long reviewsCount;
    private String profilePictureBase64;
}
