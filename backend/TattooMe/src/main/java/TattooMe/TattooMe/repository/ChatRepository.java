package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
    List<Chat> findByInitiator_IdOrReceiver_Id(UUID initiatorId, UUID receiverId);
    Optional<Chat> findByInitiator_IdAndReceiver_Id(UUID initiatorId, UUID receiverId);
}
