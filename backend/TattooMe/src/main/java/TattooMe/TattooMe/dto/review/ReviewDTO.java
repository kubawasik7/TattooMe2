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
public class ReviewDTO {
    private UUID id;
    private int rating;
    private String content;
    private LocalDateTime createdAt;
    private String clientName;
    private String artistName;
    private List<ReviewAnswerDTO> answers;
}
