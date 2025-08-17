package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.CreateStudioDTO;
import TattooMe.TattooMe.dto.TattooStudioDTO;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.entity.TattooStudioArtist;
import TattooMe.TattooMe.repository.TattooStudioArtistRepository;
import TattooMe.TattooMe.repository.TattooStudioRepository;
import TattooMe.TattooMe.repository.UserRepository;
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
    public List<TattooStudioDTO> getAllStudios(){
        return tattooStudioRepository.findAll().stream().map(studio -> {
            TattooStudioDTO dto = new TattooStudioDTO();
            dto.setId(studio.getId());
            dto.setName(studio.getName());
            dto.setCity(studio.getCity());
            dto.setOwnerNickname(studio.getOwner().getNickname());
            return dto;
        }).toList();
    }

    public UUID createStudio(CreateStudioDTO dto, UUID ownerId) {
        TattooStudio studio = new TattooStudio();

        studio.setName(dto.getName());
        studio.setCity(dto.getCity());
        studio.setStreet(dto.getStreet());
        studio.setStreetNumber(dto.getStreetNumber());
        studio.setPostalCode(dto.getPostalCode());
        studio.setOwner(userRepository.getReferenceById(ownerId));
        tattooStudioRepository.save(studio);

        TattooStudioArtist tattooStudioArtist = new TattooStudioArtist();
        tattooStudioArtist.setTattooStudio(studio);
        tattooStudioArtist.setUser(userRepository.getReferenceById(ownerId));
        tattooStudioArtistRepository.save(tattooStudioArtist);

        return studio.getId();
    }
}
