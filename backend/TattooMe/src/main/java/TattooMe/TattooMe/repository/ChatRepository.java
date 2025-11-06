package TattooMe.TattooMe.repository;

import TattooMe.TattooMe.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ChatRepository extends JpaRepository<Chat, UUID> {
    Optional<Chat> findByInitiator_IdAndReceiver_Id(UUID initiatorId, UUID receiverId);

    Optional<Chat> findByInitiator_IdAndTattooStudio_Id(UUID initiatorId, UUID studioId);

    @Query("""
                SELECT DISTINCT c FROM Chat c
                LEFT JOIN c.tattooStudio s
                LEFT JOIN s.artists a
                WHERE 
                    c.initiator.id = :userId 
                    OR c.receiver.id = :userId
                    OR s.owner.id = :userId
                    OR a.user.id = :userId
            """)
    List<Chat> findAllByUserParticipation(@Param("userId") UUID userId);
}
