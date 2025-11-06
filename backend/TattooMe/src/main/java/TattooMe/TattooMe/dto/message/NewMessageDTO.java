package TattooMe.TattooMe.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class NewMessageDTO {
    private UUID chatId;
    @NotBlank(message = "Treść wiadomości nie może być pusta")
    @Size(max = 1000, message = "Wiadomosc może mieć maksymalnie 1000 znakow")
    private String content;
    private String base64Attachment;
    private String studioName;
    private String studioMemberName;
}
