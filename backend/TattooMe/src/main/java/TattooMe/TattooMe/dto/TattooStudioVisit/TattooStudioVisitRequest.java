package TattooMe.TattooMe.dto.TattooStudioVisit;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TattooStudioVisitRequest {

    @NotNull(message = "Data rozpoczęcia jest wymagana")
    private LocalDateTime startDate;

    @NotNull(message = "Data zakończenia jest wymagana")
    private LocalDateTime endDate;

    private String comment;
}
