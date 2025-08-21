package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.PersonInfoDTO;
import TattooMe.TattooMe.entity.PersonInfo;
import TattooMe.TattooMe.repository.PersonInfoRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PersonInfoService {
    private final PersonInfoRepository personInfoRepository;
    private final UserRepository userRepository;

    public PersonInfoService(PersonInfoRepository personInfoRepository, UserRepository userRepository) {
        this.personInfoRepository = personInfoRepository;
        this.userRepository = userRepository;
    }
    public Optional<PersonInfo> getUserInfo(UUID userId) {
        return personInfoRepository.findByUser_Id(userId);
    }

    public PersonInfo updateUserInfo(UUID userId, PersonInfoDTO dto) {
        PersonInfo info = personInfoRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    PersonInfo newInfo = new PersonInfo();
                    newInfo.setUser(userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("dane usera z nie istnieje")));
                    return newInfo;
                });

        info.setAllergies(dto.getAllergies());
        info.setChronicDiseases(dto.getChronicDiseases());
        info.setMedicines(dto.getMedicines());
        info.setExperiences(dto.getExperiences());

        return personInfoRepository.save(info);
    }


}
