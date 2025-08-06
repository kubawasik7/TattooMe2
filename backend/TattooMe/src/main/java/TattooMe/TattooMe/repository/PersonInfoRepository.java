package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.PersonInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PersonInfoRepository extends JpaRepository<PersonInfo, UUID> {
    Optional<PersonInfo> findByUser_Id(UUID userId);

}
