package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.faq.FaqDTO;
import TattooMe.TattooMe.entity.Faq;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FaqMapper {
    FaqDTO toDTO(Faq faq);

    Faq toEntity(FaqDTO dto);
}
