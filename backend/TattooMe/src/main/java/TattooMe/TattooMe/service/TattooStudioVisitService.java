package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.TattooStudioVisit.TattooStudioVisitRequest;
import TattooMe.TattooMe.dto.TattooStudioVisit.TattooStudioVisitResponse;
import TattooMe.TattooMe.entity.Status;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.entity.TattooStudioVisit;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.TattooStudioVisitMapper;
import TattooMe.TattooMe.repository.StatusRepository;
import TattooMe.TattooMe.repository.TattooStudioRepository;
import TattooMe.TattooMe.repository.TattooStudioVisitRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TattooStudioVisitService {
    @Autowired
    private TattooStudioVisitRepository visitRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TattooStudioRepository studioRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private TattooStudioVisitMapper visitMapper;

    public TattooStudioVisitResponse createVisit(UUID studioId, TattooStudioVisitRequest request, UUID artistId) {
        Status status = statusRepository.findByName("OCZEKUJĄCA");

        User artist = userRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artysta nie istnieje"));

        TattooStudio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new EntityNotFoundException("Studio nie istnieje"));

        TattooStudioVisit visit = visitMapper.toEntity(request, studio, artist, status);
        visitRepository.save(visit);

        return visitMapper.toDto(visit);
    }

    public TattooStudioVisitResponse getById(UUID visitId) {
        TattooStudioVisit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Wizyta nie istnieje"));
        return visitMapper.toDto(visit);
    }

    public List<TattooStudioVisitResponse> getActive() {
        Status status = statusRepository.findByName("OCZEKUJĄCA");
        List<TattooStudioVisit> visits = visitRepository.findByStatus(status);
        return visits.stream()
                .map(visitMapper::toDto)
                .toList();
    }

    public List<TattooStudioVisitResponse> getPast() {
        Status confirmed = statusRepository.findByName("ZATWIERDZONA");
        List<TattooStudioVisit> visits = visitRepository.findByStatus(confirmed);
        return visits.stream()
                .map(visitMapper::toDto)
                .toList();
    }

    public List<TattooStudioVisitResponse> getCancelled() {
        Status cancelled = statusRepository.findByName("ANULOWANA");
        List<TattooStudioVisit> visits = visitRepository.findByStatus(cancelled);
        return visits.stream()
                .map(visitMapper::toDto)
                .toList();
    }

    public void confirmVisit(UUID visitId) {
        TattooStudioVisit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Wizyta nie istnieje"));
        Status confirmed = statusRepository.findByName("ZATWIERDZONA");
        visit.setStatus(confirmed);
        visitRepository.save(visit);
    }

    public void cancelVisit(UUID visitId) {
        TattooStudioVisit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new EntityNotFoundException("Wizyta nie istnieje"));
        Status cancelled = statusRepository.findByName("ANULOWANA");
        visit.setStatus(cancelled);
        visitRepository.save(visit);
    }
}