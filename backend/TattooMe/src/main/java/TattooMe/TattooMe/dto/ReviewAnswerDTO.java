package TattooMe.TattooMe.dto;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
public class ReviewAnswerDTO {
    private UUID id;
    private String content;
    private LocalDateTime createdAt;
    private String userNickname;
}