package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<Visit, UUID> {
    List<Visit> findByClient_Id(UUID clientId);
    List<Visit> findByArtist_Id(UUID artistId);
}
