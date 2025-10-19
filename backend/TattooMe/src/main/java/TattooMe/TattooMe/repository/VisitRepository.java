package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.User;
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
    @Query("SELECT v FROM visit v WHERE v.artist.id = :artistId AND v.status.name IN :statuses")
    List<Visit> findByArtistAndStatuses(@Param("artistId") UUID artistId, @Param("statuses") List<String> statuses);

    @Query("SELECT v FROM visit v WHERE v.artist.id = :artistId AND v.status.name = 'ZATWIERDZONA' AND v.artistDate.date < CURRENT_TIMESTAMP")
    List<Visit> findPastByArtist(@Param("artistId") UUID artistId);

    @Query("SELECT v FROM visit v WHERE v.artist.id = :artistId AND v.status.name = 'ANULOWANA'")
    List<Visit> findCancelledByArtist(@Param("artistId") UUID artistId);
    boolean existsByArtistDateIdAndClientId(UUID artistDateId, UUID clientId);

    UUID client(User client);
}

