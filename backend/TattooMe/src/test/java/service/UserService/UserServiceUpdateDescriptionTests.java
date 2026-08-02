package service.UserService;

import TattooMe.TattooMe.dto.user.DescriptionProfileDTO;
import TattooMe.TattooMe.dto.user.UserDTO;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.UserMapper;
import TattooMe.TattooMe.repository.UserRepository;
import TattooMe.TattooMe.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceUpdateDescriptionTests {
    @Mock
    UserRepository userRepository;
    @Mock
    UserMapper userMapper;
    @InjectMocks
    UserService userService;
    private UUID userId;
    private DescriptionProfileDTO dto;
    private UserDTO userDTO;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        dto = new DescriptionProfileDTO();
        user = new User();
    }

    @Test
    void shouldUpdateDescriptionSuccessfully() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        doNothing()
                .when(userMapper)
                .updateDescriptionFromDto(dto, user);

        when(userRepository.save(user))
                .thenReturn(user);

        when(userMapper.toDTO(user))
                .thenReturn(userDTO);

        UserDTO result =
                userService.updateDescription(userId, dto);

        assertEquals(userDTO, result);

        verify(userMapper)
                .updateDescriptionFromDto(dto, user);

        verify(userRepository)
                .save(user);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () -> userService.updateDescription(userId, dto));

        assertEquals("Użytkownik nie istnieje", entityNotFoundException.getMessage());

        verify(userMapper, never())
                .updateDescriptionFromDto(any(), any());

        verify(userRepository, never())
                .save(any());
    }
}
