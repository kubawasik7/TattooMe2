package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.offer.CreateOfferDTO;
import TattooMe.TattooMe.dto.offer.OfferDTO;
import TattooMe.TattooMe.entity.TattooStudioOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface TattooStudioOfferMapper {

    TattooStudioOffer toEntity(CreateOfferDTO dto);

    @Mapping(target = "name", constant = "Studio")
    OfferDTO toDTO(TattooStudioOffer offer);

    void updateFromDTO(CreateOfferDTO dto, @MappingTarget TattooStudioOffer entity);

    List<OfferDTO> toDTOList(List<TattooStudioOffer> offers);
}
