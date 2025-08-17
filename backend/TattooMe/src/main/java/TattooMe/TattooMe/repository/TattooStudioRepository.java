package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.TattooStudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface TattooStudioRepository extends JpaRepository<TattooStudio, UUID> {
}
