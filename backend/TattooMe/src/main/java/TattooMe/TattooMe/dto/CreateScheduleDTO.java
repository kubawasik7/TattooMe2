package TattooMe.TattooMe.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateScheduleDTO {
    private LocalDateTime dateTime;
}
