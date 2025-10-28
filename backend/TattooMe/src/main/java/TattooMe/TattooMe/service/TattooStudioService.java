package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.Featured.FeaturedDTO;
import TattooMe.TattooMe.dto.tattooStudio.CreateStudioDTO;
import TattooMe.TattooMe.dto.tattooStudio.TattooStudioDTO;
import TattooMe.TattooMe.dto.user.UserDTO;
import TattooMe.TattooMe.entity.Featured;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.entity.TattooStudioArtist;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.TattooStudioMapper;
import TattooMe.TattooMe.mapper.UserMapper;
import TattooMe.TattooMe.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Autowired
    private FeaturedRepository featuredRepository;

    public TattooStudioDTO getTattooStudioById(UUID id) {
        TattooStudio tattooStudio = tattooStudioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Studio nie znalezione"));
        return tattooStudioMapper.toDTO(tattooStudio);
    }

    public List<TattooStudioDTO> getAllStudios() {
        List<TattooStudio> studios = tattooStudioRepository.findAll();
        return tattooStudioMapper.toDTOList(studios);
    }

    public List<UserDTO> getUsersByStudioIdWithAvgRatingAndFeatured(UUID studioId) {
        TattooStudio studio = tattooStudioRepository.findById(studioId)
                .orElseThrow(() -> new EntityNotFoundException("Studio nie znalezione"));

        List<UserDTO> users = studio.getArtists().stream()
                .map(TattooStudioArtist::getUser)
                .map(user -> {
                    UserDTO dto = userRepository.findUserByIdWithRating(user.getId())
                            .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony"));

                    List<Featured> featured = featuredRepository.findAllByArtistId(user.getId());
                    List<FeaturedDTO> featuredDTOs = featured.stream()
                            .map(f -> {
                                byte[] image = f.getFlash() != null ? f.getFlash().getPicture() : f.getPortfolio().getPicture();
                                boolean isFlash = f.getFlash() != null;
                                return new FeaturedDTO(f.getId(), image, isFlash);
                            })
                            .limit(5)
                            .toList();

                    dto.setFeaturedPictures(featuredDTOs);
                    return dto;
                })
                .toList();

        return users;
    }

    public void addUserToStudio(UUID studioId, String nickname) {
        TattooStudio studio = tattooStudioRepository.findById(studioId)
                .orElseThrow(() -> new EntityNotFoundException("Studio nie znalezione"));

        User user = userRepository.findByNickname(nickname)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony"));

        boolean alreadyExists = tattooStudioArtistRepository.existsByTattooStudioAndUser(studio, user);
        if (alreadyExists) {
            throw new IllegalStateException("Użytkownik już jest członkiem tego studia");
        }

        TattooStudioArtist tsa = new TattooStudioArtist();
        tsa.setTattooStudio(studio);
        tsa.setUser(user);

        tattooStudioArtistRepository.save(tsa);
    }

    public void removeUserFromStudio(UUID studioId, UUID userId) {
        TattooStudioArtist relation = tattooStudioArtistRepository
                .findByTattooStudioIdAndUserId(studioId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Relacja studio–użytkownik nie znaleziona"));

        tattooStudioArtistRepository.delete(relation);
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
