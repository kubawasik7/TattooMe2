package service.UserService;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.Security.JwtUtil;
import TattooMe.TattooMe.dto.login.LoginRequest;
import TattooMe.TattooMe.dto.login.LoginResponse;
import TattooMe.TattooMe.dto.register.RegisterRequest;
import TattooMe.TattooMe.dto.register.RegisterResponse;
import TattooMe.TattooMe.dto.user.DescriptionProfileDTO;
import TattooMe.TattooMe.dto.user.UserDTO;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.AuthMapper;
import TattooMe.TattooMe.mapper.UserMapper;
import TattooMe.TattooMe.repository.UserRepository;
import TattooMe.TattooMe.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    @Mock
    UserMapper userMapper;
    @Mock
    JwtUtil jwtUtil;
    @Captor
    private ArgumentCaptor<User> captor;
    @InjectMocks
    private UserService userService;

    @Nested
    @DisplayName("Register user")
    class RegisterUserTests{
        private RegisterRequest registerRequest;
        private RegisterResponse registerResponse;

        @BeforeEach
        void setUpRegister() {
            registerRequest = new RegisterRequest();
            registerResponse = new RegisterResponse();
            registerRequest.setNickname("Jan");
            registerRequest.setEmail("a@b.pl");
            registerRequest.setPassword("1234");
            registerRequest.setRole("ROLE_USER");
        }

        @Test
        void shouldRegisterUserSuccessfully(){
            when(userRepository.findByNickname(registerRequest.getNickname()))
                    .thenReturn(Optional.empty());

            when(userRepository.findByEmail(registerRequest.getEmail()))
                    .thenReturn(Optional.empty());

            User user = new User();

            when(authMapper.toEntity(registerRequest))
                    .thenReturn(user);

            when(passwordEncoder.encode(registerRequest.getPassword()))
                    .thenReturn("encodedPassword");

            when(userRepository.save(user))
                    .thenReturn(user);

            when(authMapper.toResponse(user))
                    .thenReturn(registerResponse);

            RegisterResponse result =
                    userService.registerUser(registerRequest);

            assertEquals(registerResponse, result);

            verify(passwordEncoder)
                    .encode(registerRequest.getPassword());

            verify(userRepository)
                    .save(user);
        }

        @Test
        void shouldThrowExceptionWhenNicknameAlreadyExists(){
            when(userRepository.findByNickname(registerRequest.getNickname()))
                    .thenReturn(Optional.of(new User()));

            RuntimeException runtimeException =
                    assertThrows(RuntimeException.class, () -> userService.registerUser(registerRequest));

            assertEquals("Nazwa użytkownika jest już zajęta", runtimeException.getMessage());

            verify(userRepository, never()).save(any(User.class));

            verify(authMapper, never())
                    .toEntity(any());

            verify(passwordEncoder, never())
                    .encode(anyString());
        }

        @Test
        void shouldThrowExceptionWhenEmailAlreadyExists(){
            when(userRepository.findByNickname(registerRequest.getNickname()))
                    .thenReturn(Optional.empty());

            when(userRepository.findByEmail(registerRequest.getEmail()))
                    .thenReturn(Optional.of(new User()));

            ResponseStatusException responseStatusException =
                    assertThrows(ResponseStatusException.class, () -> userService.registerUser(registerRequest));

            assertEquals("Konto z tym adresem e-mail już istnieje.", responseStatusException.getReason());

            verify(userRepository, never()).save(any(User.class));

            verify(authMapper, never())
                    .toEntity(any());

            verify(passwordEncoder, never())
                    .encode(anyString());
        }

        @Test
        void shouldSaveUserWithEncodedPassword(){
            when(userRepository.findByNickname(registerRequest.getNickname()))
                    .thenReturn(Optional.empty());

            when(userRepository.findByEmail(registerRequest.getEmail()))
                    .thenReturn(Optional.empty());

            User user = new User();

            when(authMapper.toEntity(registerRequest))
                    .thenReturn(user);

            when(passwordEncoder.encode(registerRequest.getPassword()))
                    .thenReturn("encodedPassword");

            when(userRepository.save(user))
                    .thenReturn(user);

            when(authMapper.toResponse(user))
                    .thenReturn(registerResponse);

            RegisterResponse result =
                    userService.registerUser(registerRequest);

            assertEquals(registerResponse, result);


            verify(userRepository)
                    .save(captor.capture());

            User savedUser =
                    captor.getValue();

            assertEquals("encodedPassword", savedUser.getPassword());

            verify(passwordEncoder)
                    .encode(registerRequest.getPassword());
        }
    }

    @Nested
    @DisplayName("Login user")
    class LoginUserTests{
        private LoginRequest loginRequest;
        private User user;

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
        void shouldLoginUserSuccessfully(){
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

            assertEquals("Nie znaleziono użytkownika", entityNotFoundException.getMessage());

            verify(passwordEncoder, never())
                    .matches(anyString(), anyString());

            verify(jwtUtil, never())
                    .generateToken(any());
        }

        @Test
        void shouldThrowExceptionWhenPasswordIsIncorrect(){
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

    @Nested
    @DisplayName("All artists")
    class AllArtistsTests{
        @Test
        void shouldReturnTopArtists() {
            UserDTO artist1 = new UserDTO(
                    UUID.randomUUID(),
                    "janek",
                    "Jan",
                    "Kowalski",
                    "jan@test.pl",
                    "Opis artysty",
                    null,
                    4.8,
                    20L,
                    List.of()
            );

            UserDTO artist2 = new UserDTO(
                    UUID.randomUUID(),
                    "marek",
                    "Marek",
                    "Nowak",
                    "marek@test.pl",
                    "Opis",
                    null,
                    4.5,
                    15L,
                    List.of()
            );

            List<UserDTO> artists =
                    List.of(artist1, artist2);

            when(userRepository.findTopUsersWithAvgRating(any(Pageable.class)))
                    .thenReturn(artists);

            List<UserDTO> result =
                    userService.getTop5Artists();

            assertEquals(2, result.size());
            assertEquals(artists, result);

            verify(userRepository)
                    .findTopUsersWithAvgRating(any(Pageable.class));
        }

        @Test
        void shouldReturnTop5Artists(){
            when(userRepository.findTopUsersWithAvgRating(any(Pageable.class)))
                    .thenReturn(List.of());

            userService.getTop5Artists();

            ArgumentCaptor<Pageable> captor =
                    ArgumentCaptor.forClass(Pageable.class);

            verify(userRepository)
                    .findTopUsersWithAvgRating(captor.capture());

            Pageable pageable = captor.getValue();

            assertEquals(0, pageable.getPageNumber());
            assertEquals(4, pageable.getPageSize());
        }
    }

    @Nested
    @DisplayName("Update profile")
    class UpdateProfileTests{
        private User user;
        private DescriptionProfileDTO dto;
        private UUID userId;
        private UserDTO userDTO;

        @BeforeEach
        void setUpUpdateProfile() {
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
}
