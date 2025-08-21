package TattooMe.TattooMe.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class VisitDTO {
    private UUID id;
    private String comment;
    private String status;
    private LocalDateTime date;
    private String artistName;
    private String flashDescription;
}
