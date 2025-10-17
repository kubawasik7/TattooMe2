package TattooMe.TattooMe.dto.review;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private UUID id;
    @Min(value = 1, message = "Ocena musi być co najmniej 1")
    @Max(value = 5, message = "Ocena nie może być większa niż 5")
    private int rate;
    private String content;
    private LocalDateTime createdAt;
    private String authorNickname;
    private UUID targetId;
    private UUID tattooStudioId;
    private List<ReviewAnswerDTO> answers;
}
