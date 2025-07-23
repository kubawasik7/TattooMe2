package TattooMe.TattooMe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(
            name = "user_id",
            columnDefinition = "BINARY(16)",
            nullable = false,
            updatable = false
    )
    private UUID id;
    @Column(name = "password", nullable = false, length = 60)
    private String password;
    @Column(name = "nickname", nullable = false, length = 35)
    private String nickname;
    @Column(name = "email", nullable = false, length = 35)
    private String email;
    @Column(name = "name", length = 35)
    private String name;
    @Column(name = "surname", length = 45)
    private String surname;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    private byte[] profilePicture;
    @Column(name = "phone_number", length = 9)
    private String phoneNumber;
    @Column(name = "role", length = 20)
    private String role;
}
