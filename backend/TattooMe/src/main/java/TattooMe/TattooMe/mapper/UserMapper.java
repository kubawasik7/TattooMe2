package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.user.DescriptionProfileDTO;
import TattooMe.TattooMe.dto.user.UserDTO;
import TattooMe.TattooMe.dto.user.UserProfileUpdateDTO;
import TattooMe.TattooMe.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    List<UserDTO> toDTOList(List<User> users);

    void updateDescriptionFromDto(DescriptionProfileDTO dto, @MappingTarget User user);

    void updateUserFromDto(UserProfileUpdateDTO dto, @MappingTarget User user);
}