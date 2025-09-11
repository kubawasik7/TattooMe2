package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.ReviewAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewAnswerRepository extends JpaRepository<ReviewAnswer, UUID> {
    List<ReviewAnswer> findByReview_Id(UUID reviewId);
}