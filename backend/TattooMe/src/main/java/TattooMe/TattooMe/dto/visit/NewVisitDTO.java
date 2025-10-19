package TattooMe.TattooMe.dto.visit;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NewVisitDTO {
    private UUID artistDateId;
    private UUID flashId;
    private String comment;
    private String allergies;
    private String chronicDiseases;
    private String medicines;
    private String experiences;

    public boolean hasPersonInfoData() {
        return allergies != null || chronicDiseases != null || medicines != null || experiences != null;
    }
}