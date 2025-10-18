package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.message.MessageDTO;
import TattooMe.TattooMe.entity.Message;
import org.mapstruct.Mapper;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    default MessageDTO toDTO(Message message) {
        if (message == null) return null;

        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getId());
        dto.setContent(message.getContent());
        dto.setDate(message.getDate());

        if (message.getAttachment() != null) {
            dto.setBase64Attachment(Base64.getEncoder().encodeToString(message.getAttachment()));
        }

        return dto;
    }

    default List<MessageDTO> toDTOList(List<Message> messages) {
        return messages.stream()
                .map(this::toDTO)
                .toList();
    }
}