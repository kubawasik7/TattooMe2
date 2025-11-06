package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.message.MessageDTO;
import TattooMe.TattooMe.dto.message.NewMessageDTO;
import TattooMe.TattooMe.entity.Chat;
import TattooMe.TattooMe.entity.Message;
import TattooMe.TattooMe.entity.TattooStudio;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.MessageMapper;
import TattooMe.TattooMe.repository.ChatRepository;
import TattooMe.TattooMe.repository.MessageRepository;
import TattooMe.TattooMe.repository.TattooStudioRepository;
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
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private TattooStudioRepository tattooStudioRepository;

    public List<MessageDTO> getMessages(UUID chatId, UUID userId) throws AccessDeniedException {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono chatu"));

        if (!isParticipant(chat, userId)) {
            throw new AccessDeniedException("Nie jesteś uczestnikiem tego chatu");
        }

        List<Message> messages = messageRepository.findByChat_IdOrderByDateAsc(chatId);
        return messageMapper.toDTOList(messages);
    }

    public MessageDTO sendMessage(NewMessageDTO newMessageDTO, UUID senderId) throws AccessDeniedException {
        Chat chat = chatRepository.findById(newMessageDTO.getChatId())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono chatu"));

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika"));

        TattooStudio studio = tattooStudioRepository.findByArtistUserId(senderId).orElse(null);

        if (!isParticipant(chat, senderId)) {
            throw new AccessDeniedException("Nie jesteś uczestnikiem tego chatu");
        }

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setDate(LocalDateTime.now());
        message.setContent(newMessageDTO.getContent());

        if (newMessageDTO.getBase64Attachment() != null && !newMessageDTO.getBase64Attachment().isEmpty()) {
            message.setAttachment(Base64.getDecoder().decode(newMessageDTO.getBase64Attachment()));
        }

        //jesli uzytkownik należy do studia
        if (studio != null) {
            message.setSenderStudio(studio);
            message.setStudioMemberSender(sender);
        }

        messageRepository.save(message);

        return messageMapper.toDTO(message);
    }

    private boolean isParticipant(Chat chat, UUID userId) {
        //jeśli uczestnikiem jest uzytkownik
        if ((chat.getInitiator() != null && chat.getInitiator().getId().equals(userId))
                || (chat.getReceiver() != null && chat.getReceiver().getId().equals(userId))) {
            return true;
        }

        //jeśli uczestnikiem jest studio jako odbiorca to sprawdzamy właściciela i członków
        if (chat.getTattooStudio() != null) {
            TattooStudio studio = chat.getTattooStudio();

            //wlasciciel studia
            if (studio.getOwner() != null && studio.getOwner().getId().equals(userId)) {
                return true;
            }

            //czlonkowie studia
            if (studio.getArtists() != null && studio.getArtists().stream()
                    .anyMatch(artist -> artist.getUser() != null && artist.getUser().getId().equals(userId))) {
                return true;
            }
        }
        return false;
    }
}
