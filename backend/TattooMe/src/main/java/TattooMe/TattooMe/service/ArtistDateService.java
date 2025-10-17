package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.schedule.CreateScheduleDTO;
import TattooMe.TattooMe.dto.schedule.ScheduleDTO;
import TattooMe.TattooMe.entity.ArtistDate;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.ArtistDateMapper;
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
    @Autowired
    ArtistDateMapper dateMapper;

    public List<ScheduleDTO> listSlots(UUID artistId) {
        List<ArtistDate> artistDates = dateRepository.findAllByTattooArtistId(artistId);
        return dateMapper.toDTOList(artistDates);
    }

    public List<ScheduleDTO> getAvailableByArtist(UUID artistId) {
        List<ArtistDate> artistDates = dateRepository.findByTattooArtist_IdAndIsAvailableTrueOrderByDateAsc(artistId);
        return dateMapper.toDTOList(artistDates);
    }

    @Transactional
    public ScheduleDTO createSlot(UUID artistId, CreateScheduleDTO dto) {
        User user = userRepository.findById(artistId).orElseThrow();
        ArtistDate slot = new ArtistDate();

        slot.setTattooArtist(user);
        slot.setDate(dto.getDateTime());

        ArtistDate saved = dateRepository.save(slot);
        return dateMapper.toDTO(saved);
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
        return dateMapper.toDTO(slot);
    }
}
