package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.Status;
import TattooMe.TattooMe.entity.TattooStudioVisit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TattooStudioVisitRepository extends JpaRepository<TattooStudioVisit, UUID> {
    List<TattooStudioVisit> findByStatus(Status status);
}
