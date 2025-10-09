package TattooMe.TattooMe.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class CreateOfferDTO {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
}
