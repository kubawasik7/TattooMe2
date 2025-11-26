package TattooMe.TattooMe.dto.tattooStudioOffer;

import TattooMe.TattooMe.dto.flashOffer.FlashOfferDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TattooStudioOfferDTO {
    private UUID id;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<FlashOfferDTO> flashOffers = new ArrayList<>();
}