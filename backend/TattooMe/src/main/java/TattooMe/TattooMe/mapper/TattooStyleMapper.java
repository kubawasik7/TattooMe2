package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.tattooStyle.TattooStyleDTO;
import TattooMe.TattooMe.entity.TattooStyle;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TattooStyleMapper {
    TattooStyleDTO toDTO(TattooStyle style);

    List<TattooStyleDTO> toDTOList(List<TattooStyle> styles);
}
