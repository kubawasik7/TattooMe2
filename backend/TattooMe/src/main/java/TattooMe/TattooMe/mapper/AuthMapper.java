package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.register.RegisterRequest;
import TattooMe.TattooMe.dto.register.RegisterResponse;
import TattooMe.TattooMe.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    User toEntity(RegisterRequest dto);

    RegisterResponse toResponse(User user);
}