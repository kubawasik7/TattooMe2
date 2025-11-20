package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.dto.tattooStudio.TattooStudioDTO;
import TattooMe.TattooMe.entity.TattooStudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Query("""
            SELECT new TattooMe.TattooMe.dto.tattooStudio.TattooStudioDTO(
                   s.id, s.name, s.city, s.street, s.streetNumber, s.postalCode,
                   s.description, s.profilePicture, s.owner.nickname,
                   COALESCE(AVG(r.rate), 0), COUNT(r), null)
            FROM TattooStudio s
            LEFT JOIN Review r ON r.tattooStudio.id = s.id
            WHERE s.id = :id
            GROUP BY s.id
            """)
    Optional<TattooStudioDTO> findStudioByIdWithRating(@Param("id") UUID id);

    @Query("""
                SELECT new TattooMe.TattooMe.dto.tattooStudio.TattooStudioDTO(
                    s.id, s.name, s.city, s.street, s.streetNumber, s.postalCode,
                    s.description, s.profilePicture, s.owner.nickname,
                    COALESCE(AVG(r.rate), 0), COUNT(r), null)
                FROM TattooStudio s
                LEFT JOIN Review r ON r.tattooStudio.id = s.id
                GROUP BY s.id
                ORDER BY COUNT(r) DESC, COALESCE(AVG(r.rate), 0) DESC
            """)
    List<TattooStudioDTO> findAllWithRating();
}
