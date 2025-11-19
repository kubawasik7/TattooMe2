package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.FlashOffer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FlashOfferRepository extends JpaRepository<FlashOffer, UUID> {
    List<FlashOffer> findByTattooArtistOfferId(UUID offerId);
}
