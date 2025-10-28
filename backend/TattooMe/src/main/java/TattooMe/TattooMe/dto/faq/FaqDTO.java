package TattooMe.TattooMe.dto.faq;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class FaqDTO {
    private UUID id;
    private String question;
    private String answer;
}
