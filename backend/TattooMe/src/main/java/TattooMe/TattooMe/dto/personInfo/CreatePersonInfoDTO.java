package TattooMe.TattooMe.dto.personInfo;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatePersonInfoDTO {
    @Size(min = 0, max = 255, message = "Pole może mieć maksymalnie 255 znaków")
    private String allergies;

    @Size(min = 0, max = 255, message = "Pole może mieć maksymalnie 255 znaków")
    private String chronicDiseases;

    @Size(min = 0, max = 255, message = "Pole może mieć maksymalnie 255 znaków")
    private String medicines;

    @Size(min = 0, max = 255, message = "Pole może mieć maksymalnie 255 znaków")
    private String experiences;
}
