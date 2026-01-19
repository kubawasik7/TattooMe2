package TattooMe.TattooMe.dto.schedule;

import TattooMe.TattooMe.entity.ArtistDate;
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
    private boolean reserved;

    public static ScheduleDTO from(ArtistDate slot) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setId(slot.getId());
        dto.setDateTime(slot.getDate());
        dto.setAvailable(slot.isAvailable());
        dto.setReserved(slot.getVisit() != null);
        return dto;
    }
}
