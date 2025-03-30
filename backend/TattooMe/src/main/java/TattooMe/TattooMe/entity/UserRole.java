package TattooMe.TattooMe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRole {
    @Id
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    private UUID userId;

    @Column(name = "is_client", nullable = false)
    private boolean isClient;

    @Column(name = "is_tattoo_artist", nullable = false)
    private boolean isTattooArtist;

    @Column(name = "is_trainee", nullable = false)
    private boolean isTrainee;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
