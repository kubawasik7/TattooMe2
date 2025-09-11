package TattooMe.TattooMe.dto;


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
    private int rate;
    private String content;
    private LocalDateTime createdAt;
    private String authorNickname;
    private UUID targetId;
    private UUID tattooStudioId;
    private List<ReviewAnswerDTO> answers;
}
