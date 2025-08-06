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
@Table(name = "person_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonInfo {
    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(
            name = "person_info_id",
            columnDefinition = "BINARY(16)",
            nullable = false,
            updatable = false
    )
    private UUID id;
    @Column(name = "allergies")
    private String allergies;
    @Column(name = "chronic_diseases")
    private String chronicDiseases;
    @Column(name = "previous_experience")
    private String experiences;
    @Column(name = "medicines")
    private String medicines;
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
