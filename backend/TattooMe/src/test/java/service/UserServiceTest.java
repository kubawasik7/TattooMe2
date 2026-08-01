package service;

import TattooMe.TattooMe.dto.register.RegisterRequest;
import TattooMe.TattooMe.dto.register.RegisterResponse;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.AuthMapper;
import TattooMe.TattooMe.repository.UserRepository;
import TattooMe.TattooMe.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;



import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthMapper authMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserService userService;
    private RegisterRequest registerRequest;
    private RegisterResponse registerResponse;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerResponse = new RegisterResponse();
        registerRequest.setNickname("Jan");
        registerRequest.setEmail("a@b.pl");
        registerRequest.setPassword("1234");
        registerRequest.setRole("ROLE_USER");
    }

    @Test
    void shouldRegisterUserSuccessfully(){
        when(userRepository.findByNickname(registerRequest.getNickname())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());

        User user = new User();

        when(authMapper.toEntity(registerRequest)).thenReturn(user);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);
        when(authMapper.toResponse(user)).thenReturn(registerResponse);

        RegisterResponse result = userService.registerUser(registerRequest);
        assertEquals(registerResponse, result);

        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(user);
    }
    @Test
    void shouldThrowExceptionWhenNicknameAlreadyExist(){
        when(userRepository.findByNickname(registerRequest.getNickname())).thenReturn(Optional.of(new User()));

        RuntimeException runtimeException = assertThrows(RuntimeException.class, () -> userService.registerUser(registerRequest));

        assertEquals("Nazwa użytkownika jest już zajęta", runtimeException.getMessage());

        verify(userRepository, never()).save(any(User.class));

        verify(authMapper, never())
                .toEntity(any());

        verify(passwordEncoder, never())
                .encode(anyString());
    }
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists(){
        when(userRepository.findByNickname(registerRequest.getNickname())).thenReturn(Optional.empty());

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.of(new User()));

        ResponseStatusException responseStatusException = assertThrows(ResponseStatusException.class, () -> userService.registerUser(registerRequest));

        assertEquals("Konto z tym adresem e-mail już istnieje.", responseStatusException.getReason());

        verify(userRepository, never()).save(any(User.class));

        verify(authMapper, never())
                .toEntity(any());

        verify(passwordEncoder, never())
                .encode(anyString());
    }
    @Test
    void shouldSaveUserWithEncodedPassword(){
        when(userRepository.findByNickname(registerRequest.getNickname())).thenReturn(Optional.empty());

        when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());

        User user = new User();

        when(authMapper.toEntity(registerRequest)).thenReturn(user);

        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("encodedPassword");

        when(userRepository.save(user)).thenReturn(user);

        when(authMapper.toResponse(user)).thenReturn(registerResponse);

        RegisterResponse result = userService.registerUser(registerRequest);

        assertEquals(registerResponse, result);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();

        assertEquals("encodedPassword", savedUser.getPassword());

        verify(passwordEncoder).encode(registerRequest.getPassword());
    }
}
