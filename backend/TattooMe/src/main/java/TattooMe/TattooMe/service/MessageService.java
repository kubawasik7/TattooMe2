package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.message.MessageDTO;
import TattooMe.TattooMe.dto.message.NewMessageDTO;
import TattooMe.TattooMe.entity.Chat;
import TattooMe.TattooMe.entity.Message;
import TattooMe.TattooMe.mapper.MessageMapper;
import TattooMe.TattooMe.repository.ChatRepository;
import TattooMe.TattooMe.repository.MessageRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ChatRepository chatRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageMapper messageMapper;

    public List<MessageDTO> getMessages(UUID chatId, UUID userId) throws AccessDeniedException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono chatu"));

        if (!isParticipant(chat, userId)) {
            throw new AccessDeniedException("Nie jesteś uczestnikiem tego chatu");
        }

        List<Message> messages = messageRepository.findByChat_IdOrderByDateAsc(chatId);
        return messageMapper.toDTOList(messages);
    }

    @Transactional
    public MessageDTO sendMessage(NewMessageDTO newMessageDTO, UUID senderId) throws AccessDeniedException {
        Chat chat = chatRepository.findById(newMessageDTO.getChatId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono chatu"));

        if (!isParticipant(chat, senderId)) {
            throw new AccessDeniedException("Nie jesteś uczestnikiem tego chatu");
        }

        Message message = new Message();
        message.setChat(chat);
        message.setSender(userRepository.getReferenceById(senderId));
        message.setContent(newMessageDTO.getContent());
        message.setDate(LocalDateTime.now());
        message.setAttachment(Base64.getDecoder().decode(newMessageDTO.getBase64Attachment()));

        Message saved = messageRepository.save(message);
        return messageMapper.toDTO(saved);
    }

    private boolean isParticipant(Chat chat, UUID userId) {
        return chat.getInitiator().getId().equals(userId) || (chat.getReceiver() != null && chat.getReceiver().getId().equals(userId));
    }
}
