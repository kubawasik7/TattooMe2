package TattooMe.TattooMe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Table
@Entity(name = "visit")
@Getter
@Setter
public class Visit {
    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(
            name = "visit_id",
            columnDefinition = "BINARY(16)",
            nullable = false,
            updatable = false
    )
    private UUID id;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "status_id", nullable = false)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne
    @JoinColumn(name = "artist_id", nullable = false)
    private User artist;

    @OneToOne
    @JoinColumn(name = "person_info_id")
    private PersonInfo personInfo;

    @ManyToOne
    @JoinColumn(name = "artist_date_id", nullable = false)
    private ArtistDate artistDate;

    @ManyToOne
    @JoinColumn(name = "flash_id")
    private Flash flash;

    @ManyToOne
    @JoinColumn(name = "tattoo_studio_id")
    private TattooStudio tattooStudio;
}
