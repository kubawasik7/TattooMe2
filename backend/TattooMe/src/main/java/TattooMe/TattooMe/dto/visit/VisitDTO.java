package TattooMe.TattooMe.dto.visit;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class VisitDTO {
    private UUID id;
    private String status;
    private LocalDateTime date;
    private String artistName;
    private String clientName;
    private String comment;
    private String flashDescription;
    private String flashImage;
    private String tattooStudioName;
    private String allergies;
    private String chronicDiseases;
    private String medicines;
    private String experiences;
}
