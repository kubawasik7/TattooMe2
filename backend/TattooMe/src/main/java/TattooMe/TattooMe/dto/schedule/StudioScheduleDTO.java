package TattooMe.TattooMe.dto.schedule;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class StudioScheduleDTO {
    private UUID slotId;
    private LocalDateTime dateTime;
    private boolean available;

    private UUID artistId;
    private String artistNickname;
    private String artistName;
    private String artistSurname;
    private String studioRole;
}

