package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.FavoriteArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist, UUID> {
    List<FavoriteArtist> findAllByUser_Id(UUID userId);
    boolean existsByUser_IdAndArtist_Id(UUID userId, UUID artistId);
    void deleteByUser_IdAndArtist_Id(UUID userId, UUID artistId);
}
