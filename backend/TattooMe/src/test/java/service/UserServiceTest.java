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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;



import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
