package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findByInitiator_IdOrReceiver_Id(UUID initiatorId, UUID receiverId);
    Optional<Chat> findByInitiator_IdAndReceiver_Id(UUID initiatorId, UUID receiverId);
}
