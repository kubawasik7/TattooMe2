package TattooMe.TattooMe.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ScheduleDTO {
    private UUID id;
    private LocalDateTime dateTime;
    private boolean available;
}
