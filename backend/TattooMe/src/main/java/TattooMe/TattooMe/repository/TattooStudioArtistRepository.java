package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.TattooStudioArtist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TattooStudioArtistRepository extends JpaRepository<TattooStudioArtist, UUID> {
}
