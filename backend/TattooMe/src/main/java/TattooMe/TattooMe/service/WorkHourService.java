package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.workHour.WorkHourDTO;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.entity.WorkHour;
import TattooMe.TattooMe.entity.WorkHourStudio;
import TattooMe.TattooMe.entity.WorkHourStudioId;
import TattooMe.TattooMe.mapper.WorkHourMapper;
import TattooMe.TattooMe.repository.TattooStudioRepository;
import TattooMe.TattooMe.repository.WorkHourRepository;
import TattooMe.TattooMe.repository.WorkHourStudioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkHourService {

    private final WorkHourRepository workHourRepository;
    private final WorkHourStudioRepository whsRepository;
    private final TattooStudioRepository studioRepository;
    private final WorkHourMapper mapper;

    public List<WorkHourDTO> getWorkHoursForStudio(UUID studioId) {
        studioRepository.findById(studioId).orElseThrow(() -> new EntityNotFoundException("Studio nie znalezione"));
        List<WorkHour> hours = whsRepository.findAllWorkHoursByStudioId(studioId);
        return hours.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public WorkHourDTO addWorkHourToStudio(UUID studioId, WorkHourDTO dto) {
        TattooStudio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new EntityNotFoundException("Studio nie znalezione"));

        validateWorkHourDto(dto);

        WorkHour wh = new WorkHour();
        wh.setDayOfWeek(dto.getDayOfWeek().toUpperCase());
        wh.setStartTime(dto.getStartTime());
        wh.setEndTime(dto.getEndTime());
        workHourRepository.save(wh);

        WorkHourStudioId id = new WorkHourStudioId(studioId, wh.getId());
        WorkHourStudio rel = new WorkHourStudio();
        rel.setId(id);
        rel.setStudio(studio);
        rel.setWorkHour(wh);
        whsRepository.save(rel);

        return mapper.toDTO(wh);
    }

    public WorkHourDTO updateWorkHour(UUID workHourId, WorkHourDTO dto) {
        WorkHour existing = workHourRepository.findById(workHourId)
                .orElseThrow(() -> new EntityNotFoundException("Godzina nie znaleziona"));

        validateWorkHourDto(dto);

        existing.setDayOfWeek(dto.getDayOfWeek().toUpperCase());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        workHourRepository.save(existing);

        return mapper.toDTO(existing);
    }

    public void removeWorkHourFromStudio(UUID studioId, UUID workHourId) {
        WorkHourStudioId id = new WorkHourStudioId(studioId, workHourId);
        if (whsRepository.existsById(id)) {
            whsRepository.deleteById(id);

            boolean stillUsed = whsRepository.existsById(new WorkHourStudioId(studioId, workHourId));

        } else {
            throw new EntityNotFoundException("Relacja godzina-studio nie znaleziona");
        }
    }

    private void validateWorkHourDto(WorkHourDTO dto) {

        try {
            LocalTime start = LocalTime.parse(dto.getStartTime(), DateTimeFormatter.ofPattern("HH:mm"));
            LocalTime end   = LocalTime.parse(dto.getEndTime(), DateTimeFormatter.ofPattern("HH:mm"));
            if (!end.isAfter(start)) {
                throw new IllegalArgumentException("endTime musi być większe od startTime");
            }

            try {
                DayOfWeek.valueOf(dto.getDayOfWeek().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("Niepoprawny dayOfWeek. Użyj np. MONDAY, TUESDAY ...");
            }
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Czas musi być w formacie HH:mm");
        }
    }
}

