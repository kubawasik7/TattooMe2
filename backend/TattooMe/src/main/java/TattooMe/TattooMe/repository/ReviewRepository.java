package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByTarget_Id(UUID userId);

    List<Review> findByTattooStudio_Id(UUID studioId);

    Optional<Review> findByVisit_Id(UUID visitId);

    @Query("SELECT AVG(r.rate) FROM Review r WHERE r.target.id = :userId")
    Optional<Double> findAverageByTargetId(@Param("userId") UUID userId);

    Long countByTargetId(UUID id);
}