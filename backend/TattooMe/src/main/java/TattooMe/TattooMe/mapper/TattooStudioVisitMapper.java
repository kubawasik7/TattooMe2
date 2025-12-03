package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.TattooStudioVisit.TattooStudioVisitRequest;
import TattooMe.TattooMe.dto.TattooStudioVisit.TattooStudioVisitResponse;
import TattooMe.TattooMe.entity.Status;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.entity.TattooStudioVisit;


import TattooMe.TattooMe.entity.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface TattooStudioVisitMapper {

    default TattooStudioVisit toEntity(TattooStudioVisitRequest request, TattooStudio studio, User artist, Status status) {
        TattooStudioVisit visit = new TattooStudioVisit();
        visit.setStartDate(request.getStartDate());
        visit.setEndDate(request.getEndDate());
        visit.setComment(request.getComment());
        visit.setTattooStudio(studio);
        visit.setArtist(artist);
        visit.setStatus(status);
        return visit;
    }

    default TattooStudioVisitResponse toDto(TattooStudioVisit visit) {
        if (visit == null) return null;

        TattooStudioVisitResponse dto = new TattooStudioVisitResponse();
        dto.setId(visit.getId());
        dto.setArtistNickname(visit.getArtist() != null ? visit.getArtist().getNickname() : null);
        dto.setStudioName(visit.getTattooStudio() != null ? visit.getTattooStudio().getName() : null);
        dto.setStartDate(visit.getStartDate());
        dto.setEndDate(visit.getEndDate());
        dto.setComment(visit.getComment());
        dto.setStatus(visit.getStatus() != null ? visit.getStatus().getName() : null);

        return dto;
    }
}

