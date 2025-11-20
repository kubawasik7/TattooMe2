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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkHourService {
    @Autowired
    private WorkHourRepository workHourRepository;
    @Autowired
    private WorkHourStudioRepository workHourStudioRepository;
    @Autowired
    private TattooStudioRepository studioRepository;
    @Autowired
    private WorkHourMapper workHourMapper;

    public List<WorkHourDTO> getWorkHoursForStudio(UUID studioId) {
        studioRepository.findById(studioId)
                .orElseThrow(() -> new EntityNotFoundException("Studio nie znalezione"));

        List<WorkHour> hours = workHourStudioRepository.findAllWorkHoursByStudioId(studioId);

        return hours.stream()
                .map(workHourMapper::toDTO)
                .collect(Collectors.toList());
    }

    public WorkHourDTO addWorkHourToStudio(UUID studioId, WorkHourDTO dto) {
        TattooStudio studio = studioRepository.findById(studioId)
                .orElseThrow(() -> new EntityNotFoundException("Studio nie znalezione"));

        WorkHour workHour = new WorkHour();
        workHour.setDayOfWeek(dto.getDayOfWeek().toUpperCase());
        workHour.setStartTime(dto.getStartTime());
        workHour.setEndTime(dto.getEndTime());
        workHourRepository.save(workHour);

        WorkHourStudioId id = new WorkHourStudioId(studioId, workHour.getId());
        WorkHourStudio workHourStudio = new WorkHourStudio();
        workHourStudio.setId(id);
        workHourStudio.setStudio(studio);
        workHourStudio.setWorkHour(workHour);
        workHourStudioRepository.save(workHourStudio);

        return workHourMapper.toDTO(workHour);
    }

    public WorkHourDTO updateWorkHour(UUID workHourId, WorkHourDTO dto) {
        WorkHour existing = workHourRepository.findById(workHourId)
                .orElseThrow(() -> new EntityNotFoundException("Godzina nie znaleziona"));

        existing.setDayOfWeek(dto.getDayOfWeek().toUpperCase());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        workHourRepository.save(existing);

        return workHourMapper.toDTO(existing);
    }

    public void removeWorkHourFromStudio(UUID studioId, UUID workHourId) {
        WorkHourStudioId id = new WorkHourStudioId(studioId, workHourId);
        if (workHourStudioRepository.existsById(id)) {
            workHourStudioRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Relacja godzina-studio nie znaleziona");
        }
    }
}

