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
    private TattooArtistOfferRepository repo;
    @Autowired private UserRepository userRepo;

    public List<OfferDTO> getOffers(UUID artistId) {
        return repo.findAllByTattooArtistId(artistId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public OfferDTO createOffer(UUID artistId, CreateOfferDTO dto) {
        User artist = userRepo.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono artysty"));

        if(dto.getStartDate().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Data rozpoczęcia nie może być wcześniejsza niż teraz");
        }
        if(dto.getEndDate().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Data zakonczenia nie moze byc wcześniejsza niż teraz");
        }

        TattooArtistOffer e = new TattooArtistOffer();
        e.setTattooArtist(artist);
        e.setStartDate(dto.getStartDate());
        e.setEndDate(dto.getEndDate());
        e.setDescription(dto.getDescription());
        return toDto(repo.save(e));
    }

    @Transactional
    public OfferDTO updateOffer(UUID artistId, UUID offerId, CreateOfferDTO dto) throws AccessDeniedException {
        TattooArtistOffer offer = repo.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono oferty"));

        if (!offer.getTattooArtist().getId().equals(artistId)) {
            throw new AccessDeniedException("Brak dostępu");
        }

        if(dto.getStartDate().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Data rozpoczęcia nie może być wcześniejsza niż teraz");
        }
        if(dto.getEndDate().isBefore(LocalDateTime.now())){
            throw new IllegalArgumentException("Data zakonczenia nie moze byc wcześniejsza niż teraz");
        }

        offer.setStartDate(dto.getStartDate());
        offer.setEndDate(dto.getEndDate());
        offer.setDescription(dto.getDescription());
        return toDto(offer);
    }

    public void deleteOffer(UUID artistId, UUID offerId) throws AccessDeniedException {
        TattooArtistOffer e = repo.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono oferty"));
        if (!e.getTattooArtist().getId().equals(artistId)) {
            throw new AccessDeniedException("Brak dostępu");
        }
        repo.delete(e);
    }

    private OfferDTO toDto(TattooArtistOffer e) {
        OfferDTO d = new OfferDTO();
        d.setId(e.getId());
        d.setStartDate(e.getStartDate());
        d.setEndDate(e.getEndDate());
        d.setDescription(e.getDescription());
        return d;
    }
}
