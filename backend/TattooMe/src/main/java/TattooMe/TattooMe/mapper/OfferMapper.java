package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.offer.CreateOfferDTO;
import TattooMe.TattooMe.dto.offer.OfferDTO;
import TattooMe.TattooMe.entity.TattooArtistOffer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OfferMapper {
    OfferMapper INSTANCE = Mappers.getMapper(OfferMapper.class);

    TattooArtistOffer toEntity(CreateOfferDTO dto);

    @Mapping(target = "name", source = "tattooArtist.name")
    OfferDTO toDTO(TattooArtistOffer offer);

    void updateFromDTO(CreateOfferDTO dto, @MappingTarget TattooArtistOffer entity);

    List<OfferDTO> toDTOList(List<TattooArtistOffer> offers);
}
