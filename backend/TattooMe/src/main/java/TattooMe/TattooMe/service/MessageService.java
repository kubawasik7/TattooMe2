package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.MessageDTO;
import TattooMe.TattooMe.dto.NewMessageDTO;
import TattooMe.TattooMe.entity.Chat;
import TattooMe.TattooMe.entity.Message;
import TattooMe.TattooMe.repository.ChatRepository;
import TattooMe.TattooMe.repository.MessageRepository;
import TattooMe.TattooMe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    public List<MessageDTO> getMessages(UUID chatId, UUID userId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!chat.getInitiator().getId().equals(userId) &&
                (chat.getReceiver() == null || !chat.getReceiver().getId().equals(userId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        List<Message> messages = messageRepository.findByChat_IdOrderByDateAsc(chatId);

        return messages.stream().map(msg -> {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setId(msg.getId());
            messageDTO.setContent(msg.getContent());
            messageDTO.setDate(msg.getDate());
            messageDTO.setSenderId(msg.getSender().getId());
            messageDTO.setBase64Attachment(Base64.getEncoder().encodeToString(msg.getAttachment()));
            return messageDTO;
        }).toList();
    }

    public void sendMessage(NewMessageDTO newMessageDTO, UUID senderId) {
        Chat chat = chatRepository.findById(newMessageDTO.getChatId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (!chat.getInitiator().getId().equals(senderId) &&
                (chat.getReceiver() == null || !chat.getReceiver().getId().equals(senderId))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        Message message = new Message();
        message.setChat(chat);
        message.setSender(userRepository.getReferenceById(senderId));
        message.setContent(newMessageDTO.getContent());
        message.setDate(LocalDateTime.now());
        message.setAttachment(Base64.getDecoder().decode(newMessageDTO.getBase64Attachment()));

        messageRepository.save(message);
    }
}
