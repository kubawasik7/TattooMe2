package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.WorkStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkStyleRepository extends JpaRepository<WorkStyle, UUID> {
    List<WorkStyle> findByUser_Id(UUID userId);
}

