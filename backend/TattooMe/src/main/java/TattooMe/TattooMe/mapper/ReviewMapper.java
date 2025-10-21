package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.review.CreateReviewDTO;
import TattooMe.TattooMe.dto.review.ReviewDTO;
import TattooMe.TattooMe.entity.Review;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring", uses = {ReviewAnswerMapper.class})
public interface ReviewMapper {
    @Mapping(target = "answers", source = "answers")
    @Mapping(target = "clientName", source = "author.nickname")
    @Mapping(target = "artistName", source = "target.nickname")
    @Mapping(target = "rate", source = "rate")
    ReviewDTO toDTO(Review entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "visit.id", source = "visitId")
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "target", ignore = true)
    @Mapping(target = "tattooStudio", ignore = true)
    @Mapping(target = "answers", ignore = true)
    Review fromCreateDTO(CreateReviewDTO dto);
}
