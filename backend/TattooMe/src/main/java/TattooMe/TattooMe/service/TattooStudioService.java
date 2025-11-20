package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.Featured.FeaturedDTO;
import TattooMe.TattooMe.dto.schedule.StudioScheduleDTO;
import TattooMe.TattooMe.dto.tattooStudio.CreateStudioDTO;
import TattooMe.TattooMe.dto.tattooStudio.TattooStudioDTO;
import TattooMe.TattooMe.dto.user.DescriptionProfileDTO;
import TattooMe.TattooMe.dto.user.StudioArtistDTO;

import TattooMe.TattooMe.entity.ArtistDate;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.entity.TattooStudioArtist;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.enums.StudioRole;
import TattooMe.TattooMe.mapper.TattooStudioMapper;

import TattooMe.TattooMe.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
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
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ArtistDateRepository artistDateRepository;


    public TattooStudioDTO getTattooStudioById(UUID id) {
        TattooStudioDTO tattooStudio = tattooStudioRepository.findStudioByIdWithRating(id)
                .orElseThrow(() -> new EntityNotFoundException("Studio nie znalezione"));
        return tattooStudio;
    }

    public List<TattooStudioDTO> getAllStudios() {
        List<TattooStudioDTO> studios = tattooStudioRepository.findAllWithRating();
        return studios;
    }

    public List<StudioArtistDTO> getUsersByStudioIdWithAvgRatingAndFeatured(UUID studioId) {
        TattooStudio studio = tattooStudioRepository.findById(studioId)
                .orElseThrow(() -> new EntityNotFoundException("Studio nie znalezione"));

        return studio.getArtists().stream()
                .map(artistRel -> {
                    User user = artistRel.getUser();

                    Double avgRate = reviewRepository.findAverageByTargetId(user.getId()).orElse(0.0);
                    Long reviewsCount = reviewRepository.countByTargetId(user.getId());

                    List<FeaturedDTO> featuredDTOs = featuredRepository.findAllByArtistId(user.getId())
                            .stream()
                            .map(f -> {
                                byte[] image = f.getFlash() != null ? f.getFlash().getPicture() : f.getPortfolio().getPicture();
                                boolean isFlash = f.getFlash() != null;
                                return new FeaturedDTO(f.getId(), image, isFlash);
                            })
                            .limit(5)
                            .toList();

                    StudioArtistDTO dto = new StudioArtistDTO();
                    dto.setId(user.getId());
                    dto.setNickname(user.getNickname());
                    dto.setName(user.getName());
                    dto.setSurname(user.getSurname());
                    dto.setEmail(user.getEmail());
                    dto.setDescription(user.getDescription());
                    dto.setProfilePicture(user.getProfilePicture());
                    dto.setAverageRate(avgRate);
                    dto.setReviewsCount(reviewsCount);
                    dto.setFeaturedPictures(featuredDTOs);
                    dto.setStudioRole(artistRel.getRole().name());
                    dto.setStudioId(studio.getId());

                    return dto;
                })
                .toList();
    }

    public List<StudioScheduleDTO> getAllArtistSlots(UUID studioId) {
        List<TattooStudioArtist> artists = tattooStudioArtistRepository.findByTattooStudio_Id(studioId);

        List<StudioScheduleDTO> allSlots = new ArrayList<>();

        for (TattooStudioArtist sa : artists) {
            List<ArtistDate> slots = artistDateRepository.findByTattooArtist_IdAndIsAvailableTrueOrderByDateAsc(sa.getUser().getId());

            for (ArtistDate slot : slots) {
                StudioScheduleDTO dto = new StudioScheduleDTO();
                dto.setSlotId(slot.getId());
                dto.setDateTime(slot.getDate());
                dto.setAvailable(slot.isAvailable());

                dto.setArtistId(sa.getUser().getId());
                dto.setArtistNickname(sa.getUser().getNickname());
                dto.setArtistName(sa.getUser().getName());
                dto.setArtistSurname(sa.getUser().getSurname());
                dto.setStudioRole(sa.getRole().name());

                allSlots.add(dto);
            }
        }

        return allSlots;
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
        tattooStudioArtist.setRole(StudioRole.OWNER);
        tattooStudioArtistRepository.save(tattooStudioArtist);

        return tattooStudioMapper.toDTO(studio);
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

        TattooStudioArtist tattooStudioArtist = new TattooStudioArtist();
        tattooStudioArtist.setTattooStudio(studio);
        tattooStudioArtist.setUser(user);

        tattooStudioArtistRepository.save(tattooStudioArtist);
    }

    public void removeUserFromStudio(UUID studioId, UUID userId) {
        TattooStudioArtist studioMember = tattooStudioArtistRepository
                .findByTattooStudioIdAndUserId(studioId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Relacja studio–użytkownik nie znaleziona"));

        tattooStudioArtistRepository.delete(studioMember);
    }

    @Transactional
    public void updateMemberRole(UUID studioId, UUID userId, String newRole, String currentUsername) {
        TattooStudioArtist studioArtist = tattooStudioArtistRepository.findByTattooStudio_IdAndUser_Nickname(studioId, currentUsername)
                .orElseThrow(() -> new RuntimeException("Brak dostępu"));

        if (studioArtist.getRole() != StudioRole.OWNER)
            throw new RuntimeException("Tylko właściciel może nadawać role");

        TattooStudioArtist member = tattooStudioArtistRepository.findByTattooStudio_IdAndUser_Id(studioId, userId)
                .orElseThrow(() -> new RuntimeException("Nie znaleziono użytkownika w studiu"));

        member.setRole(StudioRole.valueOf(newRole));
        tattooStudioArtistRepository.save(member);
    }

    public TattooStudioDTO updateAvatar(UUID studioId, MultipartFile file) {
        TattooStudio studio = tattooStudioRepository.findById(studioId)
                .orElseThrow(() -> new RuntimeException("Studio nie znalezione"));

        try {
            studio.setProfilePicture(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Błąd przy odczycie pliku");
        }

        tattooStudioRepository.save(studio);

        return tattooStudioMapper.toDTO(studio);
    }

    public TattooStudioDTO updateDescription(UUID studioId, DescriptionProfileDTO dto) {
        TattooStudio tattooStudio = tattooStudioRepository.findById(studioId)
                .orElseThrow(() -> new EntityNotFoundException("Studio nie istnieje"));

        tattooStudioMapper.updateDescriptionFromDto(dto, tattooStudio);
        TattooStudio saved = tattooStudioRepository.save(tattooStudio);

        return tattooStudioMapper.toDTO(saved);
    }
}
