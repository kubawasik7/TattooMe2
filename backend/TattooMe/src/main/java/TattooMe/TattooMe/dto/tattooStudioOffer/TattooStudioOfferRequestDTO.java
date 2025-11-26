package TattooMe.TattooMe.dto.tattooStudioOffer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TattooStudioOfferRequestDTO {
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<UUID> flashIds;
    private List<Integer> percentOffs;
}