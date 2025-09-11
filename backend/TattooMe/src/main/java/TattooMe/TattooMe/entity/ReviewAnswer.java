package TattooMe.TattooMe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "review_answer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewAnswer {
    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "review_answer_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "content", length = 255, nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "tattoo_studio_id", nullable = false)
    private TattooStudio tattooStudio;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;
}
