package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.Flash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FlashRepository extends JpaRepository<Flash, UUID> {
    List<Flash> findAllByUser_Id(UUID userId);
}
