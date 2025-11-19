package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.flashOffer.FlashOfferDTO;
import TattooMe.TattooMe.entity.Flash;
import TattooMe.TattooMe.entity.FlashOffer;
import TattooMe.TattooMe.entity.TattooArtistOffer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FlashOfferMapper {
    private final FlashMapper flashMapper;

    public FlashOfferDTO toDTO(FlashOffer entity) {
        FlashOfferDTO dto = new FlashOfferDTO();

        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setPercentOff(entity.getPercentOff());
        dto.setFlash(flashMapper.toDTO(entity.getFlash()));

        return dto;
    }
    public FlashOffer toEntity(FlashOfferDTO dto, Flash flash, TattooArtistOffer offer) {
        FlashOffer fo = new FlashOffer();

        fo.setDescription(dto.getDescription());
        fo.setPercentOff(dto.getPercentOff());
        fo.setFlash(flash);
        fo.setTattooArtistOffer(offer);

        return fo;
    }
}
