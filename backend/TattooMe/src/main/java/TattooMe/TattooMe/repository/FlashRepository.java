package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.Flash;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FlashRepository extends JpaRepository<Flash, UUID> {
    List<Flash> findAllByUser_Id(UUID userId);
}
