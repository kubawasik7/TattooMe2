package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.offer.CreateOfferDTO;
import TattooMe.TattooMe.dto.offer.OfferDTO;
import TattooMe.TattooMe.entity.TattooArtistOffer;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.entity.TattooStudioArtist;
import TattooMe.TattooMe.entity.TattooStudioOffer;
import TattooMe.TattooMe.mapper.OfferMapper;
import TattooMe.TattooMe.mapper.TattooStudioOfferMapper;
import TattooMe.TattooMe.repository.TattooArtistOfferRepository;
import TattooMe.TattooMe.repository.TattooStudioArtistRepository;
import TattooMe.TattooMe.repository.TattooStudioOfferRepository;
import TattooMe.TattooMe.repository.TattooStudioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TattooStudioOfferService {
    @Autowired
    private TattooStudioOfferRepository studioOfferRepository;
    @Autowired
    private TattooStudioRepository tattooStudioRepository;
    @Autowired
    private TattooStudioOfferMapper studioOfferMapper;
    @Autowired
    private OfferMapper offerMapper;
    @Autowired
    private TattooArtistOfferRepository tattooArtistOfferRepository;
    @Autowired
    private TattooStudioArtistRepository tattooStudioArtistRepository;

    public List<OfferDTO> getStudioOffers(UUID studioId) {
        List<TattooStudioOffer> offers = studioOfferRepository.findAllByStudioId(studioId);
        return studioOfferMapper.toDTOList(offers);
    }

    public List<OfferDTO> getAllOffersForStudio(UUID studioId) {

        List<TattooStudioOffer> studioOffers =
                studioOfferRepository.findAllByStudioId(studioId);

        List<OfferDTO> studioDTOs = studioOfferMapper.toDTOList(studioOffers);

        List<TattooStudioArtist> artists =
                tattooStudioArtistRepository.findByTattooStudio_Id(studioId);

        List<UUID> artistIds = artists.stream()
                .map(a -> a.getUser().getId())
                .toList();

        List<TattooArtistOffer> artistOffers =
                tattooArtistOfferRepository.findAllByTattooArtistIdIn(artistIds);

        List<OfferDTO> artistDTOs = artistOffers.stream()
                .map(offer -> {
                    OfferDTO dto = offerMapper.toDTO(offer);
                    dto.setName(offer.getTattooArtist().getNickname());
                    return dto;
                })
                .toList();

        List<OfferDTO> allOffers = new ArrayList<>();
        allOffers.addAll(studioDTOs);
        allOffers.addAll(artistDTOs);

        return allOffers;
    }


    public OfferDTO createOffer(UUID studioId, CreateOfferDTO dto) {
        TattooStudio studio = tattooStudioRepository.findById(studioId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono studia"));

        if (dto.getStartDate().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Data rozpoczęcia nie może być w przeszłości");
        if (dto.getEndDate().isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Data zakończenia nie może być w przeszłości");

        TattooStudioOffer offer = studioOfferMapper.toEntity(dto);
        offer.setStudio(studio);

        return studioOfferMapper.toDTO(studioOfferRepository.save(offer));
    }

    @Transactional
    public OfferDTO updateOffer(UUID studioId, UUID offerId, CreateOfferDTO dto) throws AccessDeniedException {
        TattooStudioOffer offer = studioOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono oferty"));

        if (!offer.getStudio().getId().equals(studioId)) {
            throw new AccessDeniedException("Brak dostępu");
        }

        studioOfferMapper.updateFromDTO(dto, offer);
        return studioOfferMapper.toDTO(studioOfferRepository.save(offer));
    }

    public void deleteOffer(UUID studioId, UUID offerId) {
        TattooStudioOffer offer = studioOfferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono promocji"));

        if (!offer.getStudio().getId().equals(studioId)) {
            throw new RuntimeException("Nie możesz usunąć promocji innego studia lub artysty");
        }

        studioOfferRepository.delete(offer);
    }
}
