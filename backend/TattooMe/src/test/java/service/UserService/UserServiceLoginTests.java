package service.UserService;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.Security.JwtUtil;
import TattooMe.TattooMe.dto.login.LoginRequest;
import TattooMe.TattooMe.dto.login.LoginResponse;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.UserRepository;
import TattooMe.TattooMe.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceLoginTests {
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    JwtUtil jwtUtil;
    @InjectMocks
    private UserService userService;
    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
    User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setNickname("Jan");
        user.setPassword("encodedPassword");
        user.setRole("ROLE_USER");
        loginRequest = new LoginRequest();
        loginRequest.setNickname("Jan");
        loginRequest.setPassword("password");
    }

    @Test
    void shouldLoginUserSuccesfully(){
        when(userRepository.findByNickname(loginRequest.getNickname()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .thenReturn(true);

        String token = "jwt-token";

        when(jwtUtil.generateToken(any(CustomUserDetails.class)))
                .thenReturn(token);

        LoginResponse result =
                userService.loginUser(loginRequest);

        assertEquals(token, result.getToken());

        verify(userRepository)
                .findByNickname(loginRequest.getNickname());

        verify(jwtUtil)
                .generateToken(any());

        verify(passwordEncoder)
                .matches(loginRequest.getPassword(), user.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound(){
        when(userRepository.findByNickname(loginRequest.getNickname()))
                .thenReturn(Optional.empty());

        EntityNotFoundException entityNotFoundException =
                assertThrows(EntityNotFoundException.class, () -> userService.loginUser(loginRequest));

        assertEquals(entityNotFoundException.getMessage(), "Nie znaleziono użytkownika");

        verify(passwordEncoder, never())
                .matches(anyString(), anyString());

        verify(jwtUtil, never())
                .generateToken(any());
    }

    @Test
    void ShouldThrowExceptionWhenPasswordIsIncorrect(){
        when(userRepository.findByNickname(loginRequest.getNickname()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(loginRequest.getPassword(), user.getPassword()))
                .thenReturn(false);

        BadCredentialsException badCredentialsException =
                assertThrows(BadCredentialsException.class, () -> userService.loginUser(loginRequest));

        assertEquals("Błędny login lub hasło", badCredentialsException.getMessage());

        verify(jwtUtil, never())
                .generateToken(any());
    }
}
