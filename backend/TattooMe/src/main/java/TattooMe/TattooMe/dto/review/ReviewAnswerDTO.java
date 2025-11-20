package TattooMe.TattooMe.dto.review;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
public class ReviewAnswerDTO {
    private UUID id;
    private String content;
    private LocalDateTime createdAt;
    private String responderName;
}