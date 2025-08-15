package TattooMe.TattooMe.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ChatDTO {
    private UUID id;
    private UUID receiverId;
    private String receiverName;
}
