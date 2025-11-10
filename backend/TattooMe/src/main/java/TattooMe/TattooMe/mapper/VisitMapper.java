package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.visit.VisitDTO;
import TattooMe.TattooMe.entity.Flash;
import TattooMe.TattooMe.entity.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface VisitMapper {

    @Mapping(target = "status", source = "status.name")
    @Mapping(target = "date", source = "artistDate.date")
    @Mapping(target = "artistName", source = "artist.nickname")
    @Mapping(target = "clientName", source = "client.nickname")
    @Mapping(target = "tattooStudioName", source = "tattooStudio.name", defaultValue = "")
    @Mapping(target = "flashDescription", source = "flash.description", defaultValue = "")
    @Mapping(target = "flashImage", expression = "java(encodeFlashImage(visit.getFlash()))")
    @Mapping(target = "allergies", source = "personInfo.allergies", defaultValue = "")
    @Mapping(target = "chronicDiseases", source = "personInfo.chronicDiseases", defaultValue = "")
    @Mapping(target = "medicines", source = "personInfo.medicines", defaultValue = "")
    @Mapping(target = "experiences", source = "personInfo.experiences", defaultValue = "")
    VisitDTO toDTO(Visit visit);

    List<VisitDTO> toDTOList(List<Visit> visits);

    default String encodeFlashImage(Flash flash) {
        if (flash != null && flash.getPicture() != null) {
            return Base64.getEncoder().encodeToString(flash.getPicture());
        }
        return null;
    }
}