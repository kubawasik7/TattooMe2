package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.TattooStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface TattooStyleRepository extends JpaRepository<TattooStyle, UUID> {
}
