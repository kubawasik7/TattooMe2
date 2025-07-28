package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.ArtistDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ArtistDateRepository extends JpaRepository<ArtistDate, UUID> {
    List<ArtistDate> findAllByTattooArtistId(UUID userId);
}
