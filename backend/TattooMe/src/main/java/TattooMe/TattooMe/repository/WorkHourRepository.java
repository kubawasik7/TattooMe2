package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.WorkHour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WorkHourRepository extends JpaRepository<WorkHour, UUID> {
}

