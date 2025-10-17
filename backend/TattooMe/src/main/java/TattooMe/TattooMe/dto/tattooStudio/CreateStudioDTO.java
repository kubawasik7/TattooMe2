package TattooMe.TattooMe.dto.tattooStudio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateStudioDTO {
    private String name;
    private String city;
    private String street;
    private String streetNumber;
    private String postalCode;
}
