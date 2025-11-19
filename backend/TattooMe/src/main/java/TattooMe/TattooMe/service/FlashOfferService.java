package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.flashOffer.FlashOfferDTO;
import TattooMe.TattooMe.entity.Flash;
import TattooMe.TattooMe.entity.FlashOffer;
import TattooMe.TattooMe.entity.TattooArtistOffer;
import TattooMe.TattooMe.mapper.FlashOfferMapper;
import TattooMe.TattooMe.repository.FlashOfferRepository;
import TattooMe.TattooMe.repository.FlashRepository;
import TattooMe.TattooMe.repository.TattooArtistOfferRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FlashOfferService {

    private final FlashOfferRepository flashOfferRepository;
    private final FlashRepository flashRepository;
    private final TattooArtistOfferRepository tattooArtistOfferRepository;
    private final FlashOfferMapper flashOfferMapper;

    public List<FlashOfferDTO> getOffersByArtistOffer(UUID tattooArtistOfferId) {
        return flashOfferRepository.findByTattooArtistOfferId(tattooArtistOfferId)
                .stream()
                .map(flashOfferMapper::toDTO)
                .toList();
    }

    public FlashOfferDTO createFlashOffer(FlashOfferDTO dto) {
        Flash flash = flashRepository.findById(dto.getFlashId())
                .orElseThrow(() -> new EntityNotFoundException("Flash nie znaleziony"));

        TattooArtistOffer offer = tattooArtistOfferRepository.findById(dto.getTattooArtistOfferId())
                .orElseThrow(() -> new EntityNotFoundException("Oferta nie znaleziona"));

        FlashOffer entity = flashOfferMapper.toEntity(dto, flash, offer);

        flashOfferRepository.save(entity);

        return flashOfferMapper.toDTO(entity);
    }

    public void deleteFlashOffer(UUID id) {
        if(!flashOfferRepository.existsById(id)) {
            throw new EntityNotFoundException("Flash nie znaleziony");
        }
        flashOfferRepository.deleteById(id);
    }
}