package TattooMe.TattooMe.dto.schedule;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateScheduleDTO {
    @NotNull(message = "Data i godzina są wymagane")
    @Future(message = "Data wizyty musi być w przyszlosci")
    private LocalDateTime dateTime;
}
