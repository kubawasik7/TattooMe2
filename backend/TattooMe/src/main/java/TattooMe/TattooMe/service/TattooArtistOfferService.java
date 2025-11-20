package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.offer.CreateOfferDTO;
import TattooMe.TattooMe.dto.offer.OfferDTO;
import TattooMe.TattooMe.entity.TattooArtistOffer;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.OfferMapper;
import TattooMe.TattooMe.repository.TattooArtistOfferRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TattooArtistOfferService {
    @Autowired
    private TattooArtistOfferRepository artistOfferRepository;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private OfferMapper offerMapper;

    public List<OfferDTO> getOffers(UUID artistId) {
        List<TattooArtistOffer> offers = artistOfferRepository.findAllByTattooArtistId(artistId);

        return offerMapper.toDTOList(offers);
    }

    public OfferDTO createOffer(UUID artistId, CreateOfferDTO offerDTO) {
        User artist = userRepo.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono artysty"));

        if (offerDTO.getStartDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data rozpoczęcia nie może być wcześniejsza niż teraz");
        }
        if (offerDTO.getEndDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Data zakonczenia nie moze byc wcześniejsza niż teraz");
        }

        TattooArtistOffer offer = offerMapper.toEntity(offerDTO);
        offer.setTattooArtist(artist);

        return offerMapper.toDTO(artistOfferRepository.save(offer));
    }

    @Transactional
    public OfferDTO updateOffer(UUID artistId, UUID offerId, CreateOfferDTO dto) throws AccessDeniedException {
        TattooArtistOffer offer = artistOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono oferty"));

        if (!offer.getTattooArtist().getId().equals(artistId)) {
            throw new AccessDeniedException("Brak dostępu");
        }

        offerMapper.updateFromDTO(dto, offer);

        return offerMapper.toDTO(artistOfferRepository.save(offer));
    }

    public void deleteOffer(UUID artistId, UUID offerId) throws AccessDeniedException {
        TattooArtistOffer offer = artistOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono oferty"));

        if (!offer.getTattooArtist().getId().equals(artistId)) {
            throw new AccessDeniedException("Brak dostępu");
        }

        artistOfferRepository.delete(offer);
    }
}
