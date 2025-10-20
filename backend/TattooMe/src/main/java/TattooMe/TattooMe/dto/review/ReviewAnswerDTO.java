package TattooMe.TattooMe.dto.review;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewAnswerDTO {
    private UUID id;
    private String content;
    private LocalDateTime createdAt;
    private String artistName;
}