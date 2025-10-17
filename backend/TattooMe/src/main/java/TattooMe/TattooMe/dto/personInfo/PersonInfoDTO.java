package TattooMe.TattooMe.dto.personInfo;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonInfoDTO {

    @Size(min = 0, max = 255, message = "Pole może mieć maksymalnie 255 znaków")
    private String allergies;

    @Size(min = 0, max = 255, message = "Pole może mieć maksymalnie 255 znaków")
    private String chronicDiseases;

    @Size(min = 0, max = 255, message = "Pole może mieć maksymalnie 255 znaków")
    private String medicines;

    @Size(min = 0, max = 255, message = "Pole może mieć maksymalnie 255 znaków")
    private String experiences;
}
