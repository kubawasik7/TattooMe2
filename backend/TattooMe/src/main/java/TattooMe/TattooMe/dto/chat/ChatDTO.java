package TattooMe.TattooMe.dto.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatDTO {
    private UUID id;
    private UUID receiverId;
    private String receiverName;
    private UUID studioId;
    private String studioName;
}
