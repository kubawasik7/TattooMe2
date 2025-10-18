package TattooMe.TattooMe.dto.portfolio;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class PortfolioDTO {
    private UUID id;
    private String base64Image;
}
