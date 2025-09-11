package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    List<Review> findByTarget_Id(UUID userId);
    List<Review> findByTattooStudio_Id(UUID studioId);
}