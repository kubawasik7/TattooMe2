package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.personInfo.CreatePersonInfoDTO;
import TattooMe.TattooMe.dto.visit.NewVisitDTO;
import TattooMe.TattooMe.dto.visit.VisitDTO;
import TattooMe.TattooMe.entity.*;
import TattooMe.TattooMe.mapper.PersonInfoMapper;
import TattooMe.TattooMe.mapper.VisitMapper;
import TattooMe.TattooMe.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class VisitService {
    @Autowired
    private VisitRepository visitRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ArtistDateRepository artistDateRepository;
    @Autowired
    private FlashRepository flashRepository;
    @Autowired
    private PersonInfoRepository personInfoRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VisitMapper visitMapper;
    @Autowired
    private PersonInfoMapper personInfoMapper;
    @Autowired
    private TattooStudioRepository tattooStudioRepository;

    public VisitDTO getVisitDetails(UUID visitId) {
        Visit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Wizyta nie znaleziona"));

        return visitMapper.toDTO(visit);
    }

    public List<VisitDTO> getActiveVisits(UUID clientId) {
        List<String> statuses = List.of("OCZEKUJĄCA", "ZATWIERDZONA");

        return visitMapper.toDTOList(visitRepository.findByClientAndStatuses(clientId, statuses));
    }

    public List<VisitDTO> getPastVisits(UUID clientId) {
        return visitMapper.toDTOList(visitRepository.findPastByClient(clientId));
    }

    public List<VisitDTO> getCancelledVisits(UUID clientId) {
        return visitMapper.toDTOList(visitRepository.findCancelledByClient(clientId));
    }

    public List<VisitDTO> getActiveVisitsAsArtist(UUID artistId) {
        List<String> statuses = List.of("OCZEKUJĄCA", "ZATWIERDZONA");

        return visitMapper.toDTOList(visitRepository.findByArtistAndStatuses(artistId, statuses));
    }

    public List<VisitDTO> getPastVisitsAsArtist(UUID artistId) {
        return visitMapper.toDTOList(visitRepository.findPastByArtist(artistId));
    }

    public List<VisitDTO> getCancelledVisitsAsArtist(UUID artistId) {
        return visitMapper.toDTOList(visitRepository.findCancelledByArtist(artistId));
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

        artistDate.setAvailable(false);
        artistDateRepository.save(artistDate);

        visit.setComment(newVisitDTO.getComment());

        Status status = statusRepository.findByName("OCZEKUJĄCA");
        visit.setStatus(status);

        Optional<PersonInfo> existing = personInfoRepository.findByUser_Id(clientId);
        if (existing.isPresent()) {
            visit.setPersonInfo(existing.get());
        } else if (newVisitDTO.hasPersonInfoData()) {
            PersonInfo personInfo = personInfoMapper.fromCreateDTO(
                    new CreatePersonInfoDTO(
                            newVisitDTO.getAllergies(),
                            newVisitDTO.getChronicDiseases(),
                            newVisitDTO.getMedicines(),
                            newVisitDTO.getExperiences()
                    )
            );
            personInfo.setUser(userRepository.getReferenceById(clientId));
            personInfoRepository.save(personInfo);
            visit.setPersonInfo(personInfo);
        }

        if (newVisitDTO.getTattooStudioId() != null) {
            TattooStudio studio = tattooStudioRepository.findById(newVisitDTO.getTattooStudioId())
                    .orElseThrow(() -> new EntityNotFoundException("Brak studia tatuażu"));
            visit.setTattooStudio(studio);
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

    public boolean cancelVisitAsArtist(UUID visitId, UUID artistId) {
        Optional<Visit> optional = visitRepository.findById(visitId);
        if (optional.isPresent()) {
            Visit visit = optional.get();
            if (visit.getArtist().getId().equals(artistId) &&
                    ("OCZEKUJĄCA".equals(visit.getStatus().getName()) ||
                            "ZATWIERDZONA".equals(visit.getStatus().getName()))) {

                Status cancelled = statusRepository.findByName("ANULOWANA");
                visit.setStatus(cancelled);
                visitRepository.save(visit);
                return true;
            }
        }
        return false;
    }

    public boolean cancelVisitAsClient(UUID visitId, UUID clientId) {
        Optional<Visit> optional = visitRepository.findById(visitId);
        if (optional.isPresent()) {
            Visit visit = optional.get();
            if (visit.getClient().getId().equals(clientId) &&
                    "OCZEKUJĄCA".equals(visit.getStatus().getName())) {

                Status cancelled = statusRepository.findByName("ANULOWANA");
                visit.setStatus(cancelled);
                visitRepository.save(visit);
                return true;
            }
        }
        return false;
    }
}
