package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.dto.user.UserDTO;
import TattooMe.TattooMe.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByNickname(String nickname);

    @Query("""
            SELECT new TattooMe.TattooMe.dto.user.UserDTO(
                   u.id, u.nickname, u.name, u.surname, u.email, u.description, u.profilePicture,
                   COALESCE(AVG(r.rate), 0), COUNT(r), null)
            FROM User u
            LEFT JOIN Review r ON r.target.id = u.id
            WHERE u.id = :id
            GROUP BY u.id
            """)
    Optional<UserDTO> findUserByIdWithRating(@Param("id") UUID id);

    @Query("""
                SELECT new TattooMe.TattooMe.dto.user.UserDTO(
                    u.id, u.nickname, u.name, u.surname, u.email, u.description, u.profilePicture,
                    COALESCE(AVG(r.rate), 0), COUNT(r), null)
                FROM User u
                LEFT JOIN Review r ON r.target.id = u.id
                WHERE (:role IS NULL OR u.role = :role)
                GROUP BY u.id
                ORDER BY u.nickname
            """)
    List<UserDTO> findAllUsersWithAvgRating(@Param("role") String role);

    @Query("""
            SELECT new TattooMe.TattooMe.dto.user.UserDTO(
                   u.id, u.nickname, u.name, u.surname, u.email, u.description, u.profilePicture, 
                   COALESCE(AVG(r.rate), 0), COUNT(r), null)
            FROM User u
            LEFT JOIN Review r ON r.target.id = u.id
            WHERE NOT u.role = 'user'
            GROUP BY u.id
            ORDER BY COUNT(r) DESC, AVG(r.rate) DESC
            """)
    List<UserDTO> findTopUsersWithAvgRating(Pageable pageable);

    Optional<User> findByEmail(String email);
}
