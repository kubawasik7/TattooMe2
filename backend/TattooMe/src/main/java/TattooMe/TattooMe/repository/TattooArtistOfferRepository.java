package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.TattooArtistOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TattooArtistOfferRepository extends JpaRepository<TattooArtistOffer, UUID> {
    List<TattooArtistOffer> findAllByTattooArtistId(UUID userId);

    List<TattooArtistOffer> findAllByTattooArtistIdIn(List<UUID> artistIds);
}
