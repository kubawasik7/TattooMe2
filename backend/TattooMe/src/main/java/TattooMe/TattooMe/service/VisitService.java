package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.NewVisitDTO;
import TattooMe.TattooMe.entity.*;
import TattooMe.TattooMe.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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

    @Transactional
    public void createVisit(NewVisitDTO newVisitDTO, UUID clientId) {
        Visit visit = new Visit();

        User client = userRepository.getReferenceById(clientId);
        ArtistDate artistDate = artistDateRepository.findById(newVisitDTO.getArtistDateId())
                .orElseThrow(() -> new IllegalArgumentException("Brak terminu"));

        if (!artistDate.isAvailable()) {
            throw new IllegalStateException("Termin niedostępny");
        }

        User artist = artistDate.getTattooArtist();
        Status status = statusRepository.findByName("OCZEKUJĄCA");

        visit.setClient(client);
        visit.setArtist(artist);
        visit.setArtistDate(artistDate);
        visit.setComment(newVisitDTO.getComment());
        visit.setStatus(status);

        if (newVisitDTO.getFlashId() != null) {
            Flash flash = flashRepository.findById(newVisitDTO.getFlashId())
                    .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono flasha"));
            visit.setFlash(flash);
        }

        if (client.getPersonInfo() == null) {
            PersonInfo personInfo = new PersonInfo();
            personInfo.setUser(client);
            personInfo.setAllergies(newVisitDTO.getAllergies());
            personInfo.setChronicDiseases(newVisitDTO.getChronicDiseases());
            personInfo.setMedicines(newVisitDTO.getMedicines());
            personInfo.setExperiences(newVisitDTO.getExperiences());
            personInfoRepository.save(personInfo);
            visit.setPersonInfo(personInfo);
        } else {
            visit.setPersonInfo(client.getPersonInfo());
        }

        artistDate.setAvailable(false);

        visitRepository.save(visit);
    }
}
