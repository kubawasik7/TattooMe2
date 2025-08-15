package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.ChatDTO;
import TattooMe.TattooMe.entity.Chat;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.ChatRepository;
import TattooMe.TattooMe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    public List<ChatDTO> getUserChats(UUID userId) {
        List<Chat> chats = chatRepository.findByInitiator_IdOrReceiver_Id(userId, userId);
        List<ChatDTO> result = new ArrayList<>();

        for (Chat chat : chats) {
            User other = chat.getInitiator().getId().equals(userId) ? chat.getReceiver() : chat.getInitiator();

            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setSenderId(chat.getId());

            if (other != null) {
                chatDTO.setReceiverId(other.getId());
                chatDTO.setReceiverName(other.getNickname());
            }
            result.add(chatDTO);
        }
        return result;
    }

    public ChatDTO startChat(UUID initiatorId, UUID receiverId) {
        Optional<Chat> existing = chatRepository.findByInitiator_IdAndReceiver_Id(initiatorId, receiverId);
        if (existing.isPresent()) {
            return toDto(existing.get(), initiatorId);
        }

        Chat chat = new Chat();
        chat.setInitiator(userRepository.getReferenceById(initiatorId));
        chat.setReceiver(userRepository.getReferenceById(receiverId));
        chat = chatRepository.save(chat);

        return toDto(chat, initiatorId);
    }

    private ChatDTO toDto(Chat chat, UUID currentUserId) {
        ChatDTO dto = new ChatDTO();
        dto.setSenderId(chat.getId());

        User other = chat.getInitiator().getId().equals(currentUserId) ? chat.getReceiver() : chat.getInitiator();

        if (other != null) {
            dto.setReceiverId(other.getId());
            dto.setReceiverName(other.getNickname());
        }
        return dto;
    }
}
