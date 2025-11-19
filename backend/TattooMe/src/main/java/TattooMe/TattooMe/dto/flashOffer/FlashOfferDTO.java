package TattooMe.TattooMe.dto.flashOffer;

import TattooMe.TattooMe.dto.flash.FlashDTO;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FlashOfferDTO {
    private UUID id;
    private String description;
    @Min(value = 0, message = "Rabat nie może być mniejszy niż 0%")
    @Max(value = 100, message = "Rabat nie może być większy niż 100%")
    private Integer percentOff;
    private UUID flashId;
    private UUID tattooArtistOfferId;
    private FlashDTO flash;
}

