package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.tattooStyle.TattooStyleDTO;
import TattooMe.TattooMe.entity.TattooStyle;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.entity.WorkStyle;
import TattooMe.TattooMe.mapper.TattooStyleMapper;
import TattooMe.TattooMe.mapper.WorkStyleMapper;
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
    @Autowired
    private WorkStyleMapper workStyleMapper;
    @Autowired
    private TattooStyleMapper tattooStyleMapper;

    public List<TattooStyleDTO> getUserStyles(UUID userId) {
        List<WorkStyle> workStyles = workStyleRepository.findByUser_Id(userId);
        return workStyleMapper.toDTOList(workStyles);
    }

    public List<TattooStyleDTO> getAllStyles() {
        List<TattooStyle> tattooStyles = tattooStyleRepository.findAll();
        return tattooStyleMapper.toDTOList(tattooStyles);
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

        return workStyleMapper.toDTOList(newStyles);
    }
}