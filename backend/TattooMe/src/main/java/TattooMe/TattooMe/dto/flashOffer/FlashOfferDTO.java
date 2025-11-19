package TattooMe.TattooMe.dto.flashOffer;

import TattooMe.TattooMe.dto.flash.FlashDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FlashOfferDTO {
    private UUID id;
    private String description;
    private Integer percentOff;
    private UUID flashId;
    private UUID tattooArtistOfferId;
    private FlashDTO flash;
}

