package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.TattooArtistOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TattooArtistOfferRepository extends JpaRepository<TattooArtistOffer, UUID> {
    List<TattooArtistOffer> findAllByTattooArtistId(UUID userId);
}
