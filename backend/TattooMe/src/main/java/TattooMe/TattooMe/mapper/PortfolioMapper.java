package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.portfolio.PortfolioDTO;
import TattooMe.TattooMe.entity.Portfolio;
import org.mapstruct.Mapper;


import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PortfolioMapper {

    default PortfolioDTO toDTO(Portfolio portfolio) {
        if (portfolio == null) return null;

        PortfolioDTO dto = new PortfolioDTO();
        dto.setId(portfolio.getId());
        if (portfolio.getPicture() != null) {
            dto.setBase64Image(Base64.getEncoder().encodeToString(portfolio.getPicture()));
        }
        return dto;
    }

    List<PortfolioDTO> toDTOList(List<Portfolio> portfolios);
}