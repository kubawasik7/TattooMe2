package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.flash.FlashDTO;
import TattooMe.TattooMe.entity.Flash;
import org.mapstruct.Mapper;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FlashMapper {

    default FlashDTO toDTO(Flash flash) {
        if (flash == null) return null;

        FlashDTO dto = new FlashDTO();
        dto.setId(flash.getId());
        dto.setSizeMin(flash.getSizeMin());
        dto.setSizeMax(flash.getSizeMax());
        dto.setPriceMin(flash.getPriceMin());
        dto.setPriceMax(flash.getPriceMax());
        dto.setReccomendedPlace(flash.getReccomendedPlace());
        dto.setDescription(flash.getDescription());

        if (flash.getPicture() != null) {
            dto.setPicture(Base64.getEncoder().encodeToString(flash.getPicture()));
        }

        return dto;
    }

    default List<FlashDTO> toDTOList(List<Flash> flashes) {
        return flashes.stream().map(this::toDTO).toList();
    }
}