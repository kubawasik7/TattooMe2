package TattooMe.TattooMe.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
