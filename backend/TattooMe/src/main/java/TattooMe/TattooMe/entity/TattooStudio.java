package TattooMe.TattooMe.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tattoo_studio")
@Getter
@Setter
public class TattooStudio {
    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "tattoo_studio_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 40)
    private String city;

    @Column(nullable = false, length = 35)
    private String street;

    @Column(name = "street_number", nullable = false, length = 10)
    private String streetNumber;

    @Column(name = "postal_code", nullable = false, length = 6)
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "tattooStudio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TattooStudioArtist> artists = new ArrayList<>();
}
