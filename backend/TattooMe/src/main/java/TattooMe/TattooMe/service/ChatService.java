package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.chat.ChatDTO;
import TattooMe.TattooMe.entity.Chat;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.ChatMapper;
import TattooMe.TattooMe.repository.ChatRepository;
import TattooMe.TattooMe.repository.UserRepository;
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
    private ChatMapper chatMapper;

    public List<ChatDTO> getUserChats(UUID userId) {
        List<Chat> chats = chatRepository.findByInitiator_IdOrReceiver_Id(userId, userId);
        return chatMapper.toDTOList(chats, userId);
    }

    public ChatDTO startChat(UUID initiatorId, UUID receiverId) {
        Optional<Chat> exist = chatRepository.findByInitiator_IdAndReceiver_Id(initiatorId, receiverId);
        if (exist.isPresent()) {
            return chatMapper.toDTO(exist.get(), initiatorId);
        }

        Chat chat = new Chat();
        chat.setInitiator(userRepository.getReferenceById(initiatorId));
        chat.setReceiver(userRepository.getReferenceById(receiverId));

        Chat saved = chatRepository.save(chat);

        return chatMapper.toDTO(saved, initiatorId);
    }
}
