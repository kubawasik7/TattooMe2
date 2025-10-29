package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.workHour.WorkHourDTO;
import TattooMe.TattooMe.entity.WorkHour;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface WorkHourMapper {

    WorkHourDTO toDTO(WorkHour workHour);

    WorkHour toEntity(WorkHourDTO dto);

}

