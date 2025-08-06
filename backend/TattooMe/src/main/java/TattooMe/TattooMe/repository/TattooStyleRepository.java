package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.TattooStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TattooStyleRepository extends JpaRepository<TattooStyle, UUID> {
}
