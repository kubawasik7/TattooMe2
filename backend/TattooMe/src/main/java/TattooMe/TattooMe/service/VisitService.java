package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.NewVisitDTO;
import TattooMe.TattooMe.dto.VisitDTO;
import TattooMe.TattooMe.entity.*;
import TattooMe.TattooMe.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final StatusRepository statusRepository;
    private final ArtistDateRepository artistDateRepository;
    private final FlashRepository flashRepository;
    private final PersonInfoRepository personInfoRepository;
    private final UserRepository userRepository;
    public List<VisitDTO> getMyVisits(UUID userId, String role) {
        List<Visit> visits;

        if ("TATTOO_ARTIST".equals(role)) {
            visits = visitRepository.findByArtist_Id(userId);
        } else {
            visits = visitRepository.findByClient_Id(userId);
        }
        return visits.stream()
                .map(this::toDTO)
                .toList();
    }
    @Transactional
    public void createVisit(UUID clientId, NewVisitDTO newVisitDTO) {
        Visit visit = new Visit();
        visit.setClient(userRepository.getReferenceById(clientId));

        ArtistDate artistDate = artistDateRepository.findById(newVisitDTO.getArtistDateId())
                .orElseThrow(() -> new EntityNotFoundException("Brak terminu"));
        visit.setArtistDate(artistDate);
        visit.setArtist(artistDate.getTattooArtist());

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

    private VisitDTO toDTO(Visit visit) {
        VisitDTO dto = new VisitDTO();
        dto.setId(visit.getId());
        dto.setDate(visit.getArtistDate().getDate());
        dto.setStatus(visit.getStatus().getName());
        dto.setArtistName(visit.getArtist().getNickname());
        dto.setClientNickname(visit.getClient().getNickname());
        return dto;
    }

}
