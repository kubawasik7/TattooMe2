package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.chat.ChatDTO;
import TattooMe.TattooMe.entity.Chat;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.ChatMapper;
import TattooMe.TattooMe.repository.ChatRepository;
import TattooMe.TattooMe.repository.TattooStudioRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TattooStudioRepository tattooStudioRepository;
    ;
    @Autowired
    private ChatMapper chatMapper;

    public List<ChatDTO> getUserChats(UUID userId) {
        List<Chat> chats = chatRepository.findAllByUserParticipation(userId);
        return chatMapper.toDTOList(chats, userId);
    }

    public ChatDTO startChat(UUID initiatorId, UUID receiverId) {
        Optional<User> receiverUser = userRepository.findById(receiverId);
        Optional<TattooStudio> receiverStudio = tattooStudioRepository.findById(receiverId);

        if (receiverUser.isEmpty() && receiverStudio.isEmpty()) {
            throw new EntityNotFoundException("Nie znaleziono odbiorcy ani studia o podanym ID");
        }

        Optional<Chat> existingChat;
        if (receiverUser.isPresent()) {
            existingChat = chatRepository.findByInitiator_IdAndReceiver_Id(initiatorId, receiverId);
        } else {
            existingChat = chatRepository.findByInitiator_IdAndTattooStudio_Id(initiatorId, receiverId);
        }

        if (existingChat.isPresent()) {
            return chatMapper.toDTO(existingChat.get(), initiatorId);
        }

        Chat chat = new Chat();
        chat.setInitiator(userRepository.getReferenceById(initiatorId));

        if (receiverUser.isPresent()) {
            chat.setReceiver(receiverUser.get());
        } else {
            chat.setTattooStudio(receiverStudio.get());
        }

        Chat saved = chatRepository.save(chat);
        return chatMapper.toDTO(saved, initiatorId);
    }
}
