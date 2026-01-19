package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.schedule.ScheduleDTO;
import TattooMe.TattooMe.entity.ArtistDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistDateMapper {
    @Mapping(source = "date", target = "dateTime")
    @Mapping(target = "reserved", expression = "java(artistDate.getVisit() != null)")
    ScheduleDTO toDTO(ArtistDate artistDate);

    @Mapping(source = "dateTime", target = "date")
    ArtistDate toEntity(ScheduleDTO scheduleDTO);

    List<ScheduleDTO> toDTOList(List<ArtistDate> artistDates);
}
