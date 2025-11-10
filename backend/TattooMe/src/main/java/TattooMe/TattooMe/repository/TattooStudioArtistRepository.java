package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.entity.TattooStudioArtist;
import TattooMe.TattooMe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TattooStudioArtistRepository extends JpaRepository<TattooStudioArtist, UUID> {

    boolean existsByTattooStudioAndUser(TattooStudio tattooStudio, User user);

    Optional<TattooStudioArtist> findByTattooStudioIdAndUserId(UUID tattooStudioId, UUID userId);

    Optional<TattooStudioArtist> findByTattooStudio_IdAndUser_Nickname(UUID studioId, String username);

    Optional<TattooStudioArtist> findByTattooStudio_IdAndUser_Id(UUID studioId, UUID userId);

    List<TattooStudioArtist> findByTattooStudio_Id(UUID studioId);
}
