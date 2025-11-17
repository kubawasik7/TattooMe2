package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.tattooStudio.CreateStudioDTO;
import TattooMe.TattooMe.dto.tattooStudio.TattooStudioDTO;
import TattooMe.TattooMe.dto.user.DescriptionProfileDTO;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;


import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TattooStudioMapper {


    @Mapping(target = "owner", ignore = true)
    TattooStudio toEntity(CreateStudioDTO dto);

    TattooStudioDTO toDTO(TattooStudio tattooStudio);

    List<TattooStudioDTO> toDTOList(List<TattooStudio> studios);

    void updateDescriptionFromDto(DescriptionProfileDTO dto, @MappingTarget TattooStudio tattooStudio);

}

