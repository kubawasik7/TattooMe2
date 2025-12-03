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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TattooStudioVisitService {

    private final TattooStudioVisitRepository visitRepository;
    private final UserRepository userRepository;
    private final TattooStudioRepository studioRepository;
    private final StatusRepository statusRepository;
    private final TattooStudioVisitMapper visitMapper;

    public TattooStudioVisitResponse createVisit(UUID studioId, TattooStudioVisitRequest request, UUID artistId) {
        Status status = statusRepository.findByName("OCZEKUJĄCA");
        if (status == null) throw new RuntimeException("Status OCZEKUJĄCA nie istnieje");

        User artist = userRepository.findById(artistId)
                .orElseThrow(() -> new RuntimeException("Artysta nie istnieje"));

        TattooStudio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new RuntimeException("Studio nie istnieje"));

        TattooStudioVisit visit = visitMapper.toEntity(request, studio, artist, status);
        visitRepository.save(visit);

        return visitMapper.toDto(visit);
    }

    public TattooStudioVisitResponse getById(UUID visitId) {
        TattooStudioVisit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Wizyta nie istnieje"));
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
                .orElseThrow(() -> new RuntimeException("Wizyta nie istnieje"));
        Status confirmed = statusRepository.findByName("ZATWIERDZONA");
        visit.setStatus(confirmed);
        visitRepository.save(visit);
    }

    public void cancelVisitAsStudio(UUID visitId) {
        TattooStudioVisit visit = visitRepository.findById(visitId)
                .orElseThrow(() -> new RuntimeException("Wizyta nie istnieje"));
        Status cancelled = statusRepository.findByName("ANULOWANA");
        visit.setStatus(cancelled);
        visitRepository.save(visit);
    }
}