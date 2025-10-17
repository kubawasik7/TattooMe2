package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.schedule.CreateScheduleDTO;
import TattooMe.TattooMe.dto.schedule.ScheduleDTO;
import TattooMe.TattooMe.entity.ArtistDate;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.ArtistDateRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@Service
public class ArtistDateService {
    @Autowired
    private ArtistDateRepository dateRepository;
    @Autowired
    private UserRepository userRepository;

    public List<ScheduleDTO> listSlots(UUID artistId) {
        return dateRepository.findAllByTattooArtistId(artistId).stream()
                .map(this::toDto)
                .toList();
    }

    public List<ScheduleDTO> getAvailableByArtist(UUID artistId) {
        return dateRepository.findByTattooArtist_IdAndIsAvailableTrueOrderByDateAsc(artistId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public ScheduleDTO createSlot(UUID artistId, CreateScheduleDTO dto) {
        User user = userRepository.findById(artistId).orElseThrow();
        ArtistDate slot = new ArtistDate();

        slot.setTattooArtist(user);
        slot.setDate(dto.getDateTime());

        ArtistDate saved = dateRepository.save(slot);
        return toDto(saved);
    }

    @Transactional
    public void deleteSlot(UUID artistId, UUID slotId) throws AccessDeniedException {
        ArtistDate slot = dateRepository.findById(slotId).orElseThrow();

        if (!slot.getTattooArtist().getId().equals(artistId)) {
            throw new AccessDeniedException("Nie jesteś właścicielem");
        }

        dateRepository.delete(slot);
    }

    @Transactional
    public ScheduleDTO toggleAvailability(UUID artistId, UUID slotId) throws AccessDeniedException {
        ArtistDate slot = dateRepository.findById(slotId).orElseThrow();

        if (!slot.getTattooArtist().getId().equals(artistId)) {
            throw new AccessDeniedException("Nie jesteś właścicielem");
        }

        slot.setAvailable(!slot.isAvailable());
        return toDto(slot);
    }

    private ScheduleDTO toDto(ArtistDate artistDate) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(artistDate.getId());
        scheduleDTO.setDateTime(artistDate.getDate());
        scheduleDTO.setAvailable(artistDate.isAvailable());
        return scheduleDTO;
    }
}
