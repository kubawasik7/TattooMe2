package TattooMe.TattooMe.service;

import TattooMe.TattooMe.entity.TattooStyle;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.entity.WorkStyle;
import TattooMe.TattooMe.repository.TattooStyleRepository;
import TattooMe.TattooMe.repository.UserRepository;
import TattooMe.TattooMe.repository.WorkStyleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class WorkStyleService {

    @Autowired
    private WorkStyleRepository workStyleRepository;

    @Autowired
    private TattooStyleRepository tattooStyleRepository;
    @Autowired
    private UserRepository userRepository;

    public List<TattooStyle> getUserStyles(UUID userId) {
        return workStyleRepository.findByUser_Id(userId).stream()
                .map(WorkStyle::getTattooStyle)
                .toList();
    }
    public List<TattooStyle> getAllStyles() {
        return tattooStyleRepository.findAll();
    }
    public void saveUserStyles(UUID userId, List<UUID> styleIds) {
        workStyleRepository.deleteAll(workStyleRepository.findByUser_Id(userId));

        User user = userRepository.findById(userId).orElseThrow();

        List<WorkStyle> newStyles = styleIds.stream().map(styleId -> {
            TattooStyle style = tattooStyleRepository.findById(styleId).orElseThrow();

            WorkStyle ws = new WorkStyle();
            ws.setUser(user);
            ws.setTattooStyle(style);
            return ws;
        }).toList();

        workStyleRepository.saveAll(newStyles);
    }

}