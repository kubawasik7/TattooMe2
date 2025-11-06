package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.chat.ChatDTO;
import TattooMe.TattooMe.entity.Chat;
import TattooMe.TattooMe.entity.User;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    default ChatDTO toDTO(Chat chat, UUID currentUserId) {
        if (chat == null) return null;

        ChatDTO dto = new ChatDTO();
        dto.setId(chat.getId());

        User other = chat.getInitiator().getId().equals(currentUserId) ? chat.getReceiver() : chat.getInitiator();

        if (other != null) {
            dto.setReceiverId(other.getId());
            dto.setReceiverName(other.getNickname());
        }

        if (chat.getTattooStudio() != null) {
            dto.setStudioId(chat.getTattooStudio().getId());
            dto.setStudioName(chat.getTattooStudio().getName());
        }

        return dto;
    }

    default List<ChatDTO> toDTOList(List<Chat> chats, UUID currentUserId) {
        return chats.stream()
                .map(chat -> toDTO(chat, currentUserId))
                .collect(Collectors.toList());
    }
}