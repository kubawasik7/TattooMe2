package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.review.CreateReviewAnswerDTO;
import TattooMe.TattooMe.dto.review.ReviewAnswerDTO;
import TattooMe.TattooMe.entity.ReviewAnswer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewAnswerMapper {

    @Mapping(target = "responderName", expression = "java(getResponderName(entity))")
    ReviewAnswerDTO toDTO(ReviewAnswer entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    ReviewAnswer fromCreateDTO(CreateReviewAnswerDTO dto);

    default String getResponderName(ReviewAnswer entity) {
        if (entity.getArtist() != null) {
            return entity.getArtist().getNickname();
        } else if (entity.getUser() != null) {
            return entity.getUser().getNickname();
        }
        return "Nieznany";
    }
}