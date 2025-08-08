package TattooMe.TattooMe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "flash")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Flash {
    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(
            name = "flash_id",
            columnDefinition = "BINARY(16)",
            nullable = false,
            updatable = false
    )
    private UUID id;
    @Lob
    @Column(name = "picture", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] picture;
    @Column(name = "size_min")
    private int sizeMin;
    @Column(name = "size_max")
    private int sizeMax;
    @Column(name = "price_min")
    private int priceMin;
    @Column(name = "price_max")
    private int priceMax;
    @Column(name = "recommended_place")
    private String reccomendedPlace;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

