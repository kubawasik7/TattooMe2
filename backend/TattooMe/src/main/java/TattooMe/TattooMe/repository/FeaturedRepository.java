package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.Featured;
import TattooMe.TattooMe.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FeaturedRepository extends JpaRepository<Featured, UUID> {
    @Query("""
                SELECT f
                FROM Featured f
                WHERE f.artist.id = :artistId
            """)
    List<Featured> findAllByArtistId(@Param("artistId") UUID artistId);

    long countByArtistId(UUID artistId);

    void deleteByPortfolio(Portfolio portfolio);
}
