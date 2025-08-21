package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface StatusRepository extends JpaRepository<Status, UUID> {
    Status findByName(String name);
}
