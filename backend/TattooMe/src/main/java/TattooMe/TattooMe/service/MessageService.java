package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.message.MessageDTO;
import TattooMe.TattooMe.dto.message.NewMessageDTO;
import TattooMe.TattooMe.entity.Chat;
import TattooMe.TattooMe.entity.Message;
import TattooMe.TattooMe.repository.ChatRepository;
import TattooMe.TattooMe.repository.MessageRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<MessageDTO> getMessages(UUID chatId, UUID userId) throws AccessDeniedException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono chatu"));

        if (!chat.getInitiator().getId().equals(userId) &&
                (chat.getReceiver() == null || !chat.getReceiver().getId().equals(userId))) {
            throw new AccessDeniedException("Nie jestes uczestnikiem tego chatu");
        }

        return messageRepository.findByChat_IdOrderByDateAsc(chatId).stream()
                .map(this::toDto)
                .toList();
    }

    public MessageDTO sendMessage(NewMessageDTO newMessageDTO, UUID senderId) throws AccessDeniedException {
        Chat chat = chatRepository.findById(newMessageDTO.getChatId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono chatu"));

        if (!chat.getInitiator().getId().equals(senderId) &&
                (chat.getReceiver() == null || !chat.getReceiver().getId().equals(senderId))) {
            throw new AccessDeniedException("Nie jestes uczestnikiem tego chatu");
        }

        Message message = new Message();
        message.setChat(chat);
        message.setSender(userRepository.getReferenceById(senderId));
        message.setContent(newMessageDTO.getContent());
        message.setDate(LocalDateTime.now());
        message.setAttachment(Base64.getDecoder().decode(newMessageDTO.getBase64Attachment()));

        return toDto(messageRepository.save(message));
    }

    private MessageDTO toDto(Message message) {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(message.getId());
        messageDTO.setContent(message.getContent());
        messageDTO.setDate(message.getDate());
        messageDTO.setSenderId(message.getSender().getId());
        messageDTO.setBase64Attachment(Base64.getEncoder().encodeToString(message.getAttachment()));
        return messageDTO;
    }
}
