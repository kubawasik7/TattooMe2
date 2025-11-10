package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.TattooStudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TattooStudioRepository extends JpaRepository<TattooStudio, UUID> {
    @Query("""
                SELECT s FROM TattooStudio s
                JOIN TattooStudioArtist sa ON sa.tattooStudio = s
                WHERE sa.user.id = :artistId
            """)
    Optional<TattooStudio> findByArtistUserId(@Param("artistId") UUID artistId);
}
