package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.personInfo.CreatePersonInfoDTO;
import TattooMe.TattooMe.dto.personInfo.PersonInfoDTO;
import TattooMe.TattooMe.entity.PersonInfo;
import TattooMe.TattooMe.mapper.PersonInfoMapper;
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
    @Autowired
    private PersonInfoMapper personInfoMapper;

    public Optional<PersonInfoDTO> getUserInfo(UUID userId) {
        return personInfoRepository.findByUser_Id(userId)
                .map(personInfoMapper::toDTO);
    }

    public PersonInfoDTO updateUserInfo(UUID userId, CreatePersonInfoDTO dto) {
        PersonInfo info = personInfoRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    PersonInfo newInfo = new PersonInfo();
                    newInfo.setUser(userRepository.findById(userId)
                            .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie istnieje")));
                    return newInfo;
                });

        personInfoMapper.updateFromDTO(dto, info);

        return personInfoMapper.toDTO(personInfoRepository.save(info));
    }
}
