package TattooMe.TattooMe.dto.TattooStudioVisit;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class TattooStudioVisitResponse {
    private UUID id;
    private String artistNickname;
    private String studioName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String comment;
    private String status;
}
