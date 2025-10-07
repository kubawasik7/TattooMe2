package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.CreateOfferDTO;
import TattooMe.TattooMe.dto.OfferDTO;
import TattooMe.TattooMe.entity.TattooArtistOffer;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.TattooArtistOfferRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TattooArtistOfferService {
    @Autowired
    private TattooArtistOfferRepository artistOfferRepository;
    @Autowired
    private UserRepository userRepo;

    public List<OfferDTO> getOffers(UUID artistId) {
        return artistOfferRepository.findAllByTattooArtistId(artistId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
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

        TattooArtistOffer tattooArtistOffer = new TattooArtistOffer();
        tattooArtistOffer.setTattooArtist(artist);
        tattooArtistOffer.setStartDate(offerDTO.getStartDate());
        tattooArtistOffer.setEndDate(offerDTO.getEndDate());
        tattooArtistOffer.setDescription(offerDTO.getDescription());

        return toDto(artistOfferRepository.save(tattooArtistOffer));
    }

    @Transactional
    public OfferDTO updateOffer(UUID artistId, UUID offerId, CreateOfferDTO dto) throws AccessDeniedException {
        TattooArtistOffer offer = artistOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono oferty"));

        if (!offer.getTattooArtist().getId().equals(artistId)) {
            throw new AccessDeniedException("Brak dostępu");
        }

        offer.setStartDate(dto.getStartDate());
        offer.setEndDate(dto.getEndDate());
        offer.setDescription(dto.getDescription());

        return toDto(offer);
    }

    public void deleteOffer(UUID artistId, UUID offerId) throws AccessDeniedException {
        TattooArtistOffer offer = artistOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono oferty"));

        if (!offer.getTattooArtist().getId().equals(artistId)) {
            throw new AccessDeniedException("Brak dostępu");
        }

        artistOfferRepository.delete(offer);
    }

    private OfferDTO toDto(TattooArtistOffer offer) {
        OfferDTO offerDTO = new OfferDTO();
        offerDTO.setId(offer.getId());
        offerDTO.setStartDate(offer.getStartDate());
        offerDTO.setEndDate(offer.getEndDate());
        offerDTO.setDescription(offer.getDescription());
        return offerDTO;
    }
}
