package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VisitRepository extends JpaRepository<Visit, UUID> {

    @Query("SELECT v FROM visit v WHERE v.client.id = :clientId AND v.status.name IN :statuses")
    List<Visit> findByClientAndStatuses(@Param("clientId") UUID clientId, @Param("statuses") List<String> statuses);

    @Query("SELECT v FROM visit v WHERE v.client.id = :clientId AND v.status.name = 'ZATWIERDZONA' AND v.artistDate.date < CURRENT_TIMESTAMP")
    List<Visit> findPastByClient(@Param("clientId") UUID clientId);

    @Query("SELECT v FROM visit v WHERE v.client.id = :clientId AND v.status.name = 'ANULOWANA'")
    List<Visit> findCancelledByClient(@Param("clientId") UUID clientId);
}

