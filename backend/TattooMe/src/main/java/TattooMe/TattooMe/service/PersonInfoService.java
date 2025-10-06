package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.PersonInfoDTO;
import TattooMe.TattooMe.entity.PersonInfo;
import TattooMe.TattooMe.repository.PersonInfoRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PersonInfoService {
    @Autowired
    private PersonInfoRepository personInfoRepository;
    @Autowired
    private UserRepository userRepository;

    public Optional<PersonInfoDTO> getUserInfo(UUID userId) {
        return personInfoRepository.findByUser_Id(userId)
                .map(this::toDto);
    }

    public PersonInfoDTO updateUserInfo(UUID userId, PersonInfoDTO dto) {
        PersonInfo info = personInfoRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    PersonInfo newInfo = new PersonInfo();
                    newInfo.setUser(userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie istnieje")));
                    return newInfo;
                });

        info.setAllergies(dto.getAllergies());
        info.setChronicDiseases(dto.getChronicDiseases());
        info.setMedicines(dto.getMedicines());
        info.setExperiences(dto.getExperiences());

        PersonInfo saved = personInfoRepository.save(info);
        return toDto(saved);
    }

    private PersonInfoDTO toDto(PersonInfo entity) {
        PersonInfoDTO dto = new PersonInfoDTO();
        dto.setAllergies(entity.getAllergies());
        dto.setChronicDiseases(entity.getChronicDiseases());
        dto.setMedicines(entity.getMedicines());
        dto.setExperiences(entity.getExperiences());
        return dto;
    }
}
