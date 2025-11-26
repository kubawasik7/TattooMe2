package TattooMe.TattooMe.dto.offer;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class OfferDTO {
    private UUID id;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String description;
    private String name;
}
