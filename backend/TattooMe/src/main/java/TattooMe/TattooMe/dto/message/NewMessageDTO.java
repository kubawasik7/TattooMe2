package TattooMe.TattooMe.dto.message;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class NewMessageDTO {
    private UUID chatId;
    private String content;
    private String base64Attachment;
}
