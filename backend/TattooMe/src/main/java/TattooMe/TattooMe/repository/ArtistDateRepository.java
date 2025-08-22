package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.ArtistDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface ArtistDateRepository extends JpaRepository<ArtistDate, UUID> {
    List<ArtistDate> findAllByTattooArtistId(UUID userId);
    List<ArtistDate> findByTattooArtist_IdAndIsAvailableTrueOrderByDateAsc(UUID artistId);
}
