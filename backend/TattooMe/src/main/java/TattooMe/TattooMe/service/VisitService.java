package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.visit.NewVisitDTO;
import TattooMe.TattooMe.dto.visit.VisitDTO;
import TattooMe.TattooMe.entity.*;
import TattooMe.TattooMe.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final StatusRepository statusRepository;
    private final ArtistDateRepository artistDateRepository;
    private final FlashRepository flashRepository;
    private final PersonInfoRepository personInfoRepository;
    private final UserRepository userRepository;

    public VisitDTO getVisitDetails(UUID visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Visit not found"));
        return toDTO(visit);
    }
    public List<VisitDTO> getActiveVisits(UUID clientId) {
        List<String> statuses = List.of("OCZEKUJĄCA", "ZATWIERDZONA");
        return visitRepository.findByClientAndStatuses(clientId, statuses)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<VisitDTO> getPastVisits(UUID clientId) {
        return visitRepository.findPastByClient(clientId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<VisitDTO> getCancelledVisits(UUID clientId) {
        return visitRepository.findCancelledByClient(clientId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    public List<VisitDTO> getActiveVisitsAsArtist(UUID artistId) {
        List<String> statuses = List.of("OCZEKUJĄCA", "ZATWIERDZONA");
        return visitRepository.findByArtistAndStatuses(artistId, statuses)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<VisitDTO> getPastVisitsAsArtist(UUID artistId) {
        return visitRepository.findPastByArtist(artistId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<VisitDTO> getCancelledVisitsAsArtist(UUID artistId) {
        return visitRepository.findCancelledByArtist(artistId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    public boolean cancelVisitAsArtist(UUID visitId, UUID artistId) {
        Optional<Visit> optional = visitRepository.findById(visitId);
        if (optional.isPresent()) {
            Visit visit = optional.get();

            if (visit.getArtist().getId().equals(artistId) &&
                    ("OCZEKUJĄCA".equals(visit.getStatus().getName()) || "ZATWIERDZONA".equals(visit.getStatus().getName()))) {

                Status cancelledStatus = statusRepository.findByName("ANULOWANA");
                visit.setStatus(cancelledStatus);
                visitRepository.save(visit);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void createVisit(UUID clientId, NewVisitDTO newVisitDTO) {
        Visit visit = new Visit();
        visit.setClient(userRepository.getReferenceById(clientId));

        ArtistDate artistDate = artistDateRepository.findById(newVisitDTO.getArtistDateId())
                .orElseThrow(() -> new EntityNotFoundException("Brak terminu"));
        visit.setArtistDate(artistDate);
        visit.setArtist(artistDate.getTattooArtist());
        boolean exists = visitRepository.existsByArtistDateIdAndClientId(newVisitDTO.getArtistDateId(), clientId);
        if (exists) {
            throw new IllegalStateException("Ten termin jest już zarezerwowany!");
        }


        if (newVisitDTO.getFlashId() != null) {
            Flash flash = flashRepository.findById(newVisitDTO.getFlashId())
                    .orElseThrow(() -> new EntityNotFoundException("Brak flasha"));
            visit.setFlash(flash);
        }

        visit.setComment(newVisitDTO.getComment());

        Status status = statusRepository.findByName("OCZEKUJĄCA");
        visit.setStatus(status);

        Optional<PersonInfo> existing = personInfoRepository.findByUser_Id(clientId);
        if (existing.isPresent()) {
            visit.setPersonInfo(existing.get());
        } else if (
                newVisitDTO.getAllergies() != null || newVisitDTO.getChronicDiseases() != null ||
                        newVisitDTO.getMedicines() != null || newVisitDTO.getExperiences() != null
        ) {
            PersonInfo personInfo = new PersonInfo();
            personInfo.setUser(userRepository.getReferenceById(clientId));
            personInfo.setAllergies(newVisitDTO.getAllergies());
            personInfo.setChronicDiseases(newVisitDTO.getChronicDiseases());
            personInfo.setMedicines(newVisitDTO.getMedicines());
            personInfo.setExperiences(newVisitDTO.getExperiences());
            personInfoRepository.save(personInfo);
            visit.setPersonInfo(personInfo);
        }
        visitRepository.save(visit);
    }
    public boolean confirmVisit(UUID visitId, UUID artistId) {
        Optional<Visit> optional = visitRepository.findById(visitId);
        if (optional.isPresent()) {
            Visit visit = optional.get();
            if (visit.getArtist().getId().equals(artistId) && visit.getStatus().getName().equals("OCZEKUJĄCA")) {
                Status approvedStatus = statusRepository.findByName("ZATWIERDZONA");
                visit.setStatus(approvedStatus);
                visitRepository.save(visit);
                return true;
            }
        }
        return false;
    }


    private VisitDTO toDTO(Visit visit) {
        VisitDTO dto = new VisitDTO();
        dto.setId(visit.getId());
        dto.setStatus(visit.getStatus().getName());
        dto.setDate(visit.getArtistDate().getDate());
        dto.setArtistName(visit.getArtist().getNickname());
        dto.setClientName(visit.getClient().getNickname());
        dto.setComment(visit.getComment());

        if (visit.getFlash() != null) {
            dto.setFlashDescription(visit.getFlash().getDescription());

            if (visit.getFlash().getPicture() != null) {
                String base64 = Base64.getEncoder().encodeToString(visit.getFlash().getPicture());
                dto.setFlashImage(base64);
            }
        }

        if (visit.getTattooStudio() != null) {
            dto.setTattooStudioName(visit.getTattooStudio().getName());
        }

        if (visit.getPersonInfo() != null) {
            dto.setAllergies(visit.getPersonInfo().getAllergies());
            dto.setChronicDiseases(visit.getPersonInfo().getChronicDiseases());
            dto.setMedicines(visit.getPersonInfo().getMedicines());
            dto.setExperiences(visit.getPersonInfo().getExperiences());
        }

        return dto;
    }


}
