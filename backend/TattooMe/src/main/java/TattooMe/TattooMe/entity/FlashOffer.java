package TattooMe.TattooMe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "flash_offer")
@Getter
@Setter
public class FlashOffer {

    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(
            name = "flash_offer_id",
            columnDefinition = "BINARY(16)",
            nullable = false,
            updatable = false
    )
    private UUID id;

    @Column(name = "description")
    private String description;

    @Column(name = "percent_off")
    private Integer percentOff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tattoo_artist_offer_id")
    private TattooArtistOffer tattooArtistOffer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flash_id")
    private Flash flash;
}
