package TattooMe.TattooMe.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class MessageDTO {
    private UUID id;
    private String content;
    private LocalDateTime date;
    private UUID senderId;
    private String base64Attachment;
}
