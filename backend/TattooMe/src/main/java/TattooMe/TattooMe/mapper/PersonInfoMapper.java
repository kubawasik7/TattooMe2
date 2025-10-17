package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.personInfo.CreatePersonInfoDTO;
import TattooMe.TattooMe.dto.personInfo.PersonInfoDTO;
import TattooMe.TattooMe.entity.PersonInfo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PersonInfoMapper {
    PersonInfoDTO toDTO(PersonInfo entity);

    void updateFromDTO(CreatePersonInfoDTO dto, @MappingTarget PersonInfo entity);
}
