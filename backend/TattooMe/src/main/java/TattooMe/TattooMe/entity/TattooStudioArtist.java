package TattooMe.TattooMe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tattoo_studio_artist")
@Getter
@Setter
@NoArgsConstructor
@IdClass(TattooStudioArtistId.class)
public class TattooStudioArtist {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tattoo_studio_id")
    private TattooStudio tattooStudio;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}