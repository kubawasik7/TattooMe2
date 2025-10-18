package TattooMe.TattooMe.dto.flash;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FlashDTO {
    private UUID id;
    private Integer sizeMin;
    private Integer sizeMax;
    private Integer priceMin;
    private Integer priceMax;
    private String reccomendedPlace;
    private String description;
    private String picture;
}