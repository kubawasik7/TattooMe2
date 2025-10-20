package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.review.CreateReviewAnswerDTO;
import TattooMe.TattooMe.dto.review.ReviewAnswerDTO;
import TattooMe.TattooMe.entity.ReviewAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewAnswerMapper {

    @Mapping(target = "artistName", source = "artist.nickname")
    ReviewAnswerDTO toDTO(ReviewAnswer entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    ReviewAnswer fromCreateDTO(CreateReviewAnswerDTO dto);
}