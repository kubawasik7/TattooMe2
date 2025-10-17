package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.tattooStudio.CreateStudioDTO;
import TattooMe.TattooMe.dto.tattooStudio.TattooStudioDTO;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.entity.TattooStudioArtist;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.TattooStudioMapper;
import TattooMe.TattooMe.repository.TattooStudioArtistRepository;
import TattooMe.TattooMe.repository.TattooStudioRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TattooStudioService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TattooStudioRepository tattooStudioRepository;
    @Autowired
    private TattooStudioArtistRepository tattooStudioArtistRepository;
    @Autowired
    private TattooStudioMapper tattooStudioMapper;

    public List<TattooStudioDTO> getAllStudios() {
        List<TattooStudio> studios = tattooStudioRepository.findAll();
        return tattooStudioMapper.toDTOList(studios);
    }

    @Transactional
    public TattooStudioDTO createStudio(CreateStudioDTO dto, UUID ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new EntityNotFoundException("Uzytkownik nie znaleziony"));

        TattooStudio studio = tattooStudioMapper.toEntity(dto);
        studio.setOwner(owner);

        tattooStudioRepository.save(studio);

        TattooStudioArtist tattooStudioArtist = new TattooStudioArtist();
        tattooStudioArtist.setTattooStudio(studio);
        tattooStudioArtist.setUser(owner);
        tattooStudioArtistRepository.save(tattooStudioArtist);

        return tattooStudioMapper.toDTO(studio);
    }
}
