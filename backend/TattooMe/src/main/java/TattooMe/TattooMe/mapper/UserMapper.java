package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.user.DescriptionProfileDTO;
import TattooMe.TattooMe.dto.user.UserDTO;
import TattooMe.TattooMe.dto.user.UserProfileUpdateDTO;
import TattooMe.TattooMe.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Base64;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "profilePicture", expression = "java(mapProfilePicture(user.getProfilePicture()))")
    UserDTO toDTO(User user);

    List<UserDTO> toDTOList(List<User> users);

    default String mapProfilePicture(byte[] picture) {
        if (picture == null) return null;
        return Base64.getEncoder().encodeToString(picture);
    }

    void updateDescriptionFromDto(DescriptionProfileDTO dto, @MappingTarget User user);

    void updateUserFromDto(UserProfileUpdateDTO dto, @MappingTarget User user);
}