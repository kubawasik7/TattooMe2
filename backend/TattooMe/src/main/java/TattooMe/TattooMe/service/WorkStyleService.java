package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.TattooStyleDTO;
import TattooMe.TattooMe.entity.TattooStyle;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.entity.WorkStyle;
import TattooMe.TattooMe.repository.TattooStyleRepository;
import TattooMe.TattooMe.repository.UserRepository;
import TattooMe.TattooMe.repository.WorkStyleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class WorkStyleService {
    @Autowired
    private WorkStyleRepository workStyleRepository;
    @Autowired
    private TattooStyleRepository tattooStyleRepository;
    @Autowired
    private UserRepository userRepository;

    public List<TattooStyleDTO> getUserStyles(UUID userId) {
        return workStyleRepository.findByUser_Id(userId).stream()
                .map(ws -> new TattooStyleDTO(ws.getTattooStyle().getId(), ws.getTattooStyle().getName()))
                .collect(Collectors.toList());
    }

    public List<TattooStyleDTO> getAllStyles() {
        return tattooStyleRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<TattooStyleDTO> saveUserStyles(UUID userId, List<UUID> styleIds) {
        workStyleRepository.deleteAll(workStyleRepository.findByUser_Id(userId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika"));

        List<WorkStyle> newStyles = styleIds.stream().map(styleId -> {
            TattooStyle style = tattooStyleRepository.findById(styleId)
                    .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono stylu"));

            WorkStyle ws = new WorkStyle();
            ws.setUser(user);
            ws.setTattooStyle(style);
            return ws;
        }).toList();

        workStyleRepository.saveAll(newStyles);

        return newStyles.stream()
                .map(ws -> new TattooStyleDTO(ws.getTattooStyle().getId(), ws.getTattooStyle().getName()))
                .collect(Collectors.toList());
    }

    private TattooStyleDTO toDto(TattooStyle style) {
        TattooStyleDTO dto = new TattooStyleDTO();
        dto.setId(style.getId());
        dto.setName(style.getName());
        return dto;
    }
}