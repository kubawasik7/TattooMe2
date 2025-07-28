package TattooMe.TattooMe.dto;

import java.time.LocalDateTime;

public class CreateScheduleDTO {
    private LocalDateTime dateTime;

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
