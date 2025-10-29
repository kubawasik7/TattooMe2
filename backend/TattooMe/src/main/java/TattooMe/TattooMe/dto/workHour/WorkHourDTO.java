package TattooMe.TattooMe.dto.workHour;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
public class WorkHourDTO {
    private UUID id;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
