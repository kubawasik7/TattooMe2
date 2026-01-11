package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.FavoriteArtist;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist, UUID> {
    List<FavoriteArtist> findAllByUser_Id(UUID userId);

    boolean existsByUser_IdAndArtist_Id(UUID userId, UUID artistId);

    @Transactional
    void deleteByUser_IdAndArtist_Id(UUID userId, UUID artistId);

    boolean existsByUserIdAndArtistId(UUID userId, UUID artistId);
}
