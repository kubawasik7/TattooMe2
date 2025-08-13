package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByNickname(String nickname);
    List<User> findAllByRole(String role);
}
