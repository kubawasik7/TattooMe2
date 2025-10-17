package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.tattooStyle.TattooStyleDTO;
import TattooMe.TattooMe.entity.WorkStyle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkStyleMapper {
    @Mapping(source = "tattooStyle.id", target = "id")
    @Mapping(source = "tattooStyle.name", target = "name")
    TattooStyleDTO toDTO(WorkStyle workStyle);

    List<TattooStyleDTO> toDTOList(List<WorkStyle> workStyles);
}
