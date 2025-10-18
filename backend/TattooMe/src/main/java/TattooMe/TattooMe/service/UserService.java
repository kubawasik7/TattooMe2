package TattooMe.TattooMe.service;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.Security.JwtUtil;
import TattooMe.TattooMe.dto.login.LoginRequest;
import TattooMe.TattooMe.dto.login.LoginResponse;
import TattooMe.TattooMe.dto.register.RegisterRequest;
import TattooMe.TattooMe.dto.register.RegisterResponse;
import TattooMe.TattooMe.dto.user.DescriptionProfileDTO;
import TattooMe.TattooMe.dto.user.UserDTO;
import TattooMe.TattooMe.dto.user.UserProfileUpdateDTO;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.AuthMapper;
import TattooMe.TattooMe.mapper.UserMapper;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthMapper authMapper;

    public List<UserDTO> getUsersByRole(String role) {
        return userMapper.toDTOList(userRepository.findAllByRole(role));
    }

    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika"));
        return userMapper.toDTO(user);
    }


    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByNickname(registerRequest.getNickname()).isPresent()) {
            throw new RuntimeException("Nazwa użytkownika jest już zajęta");
        }

        User user = authMapper.toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User saved = userRepository.save(user);

        return authMapper.toResponse(saved);
    }

    public LoginResponse loginUser(LoginRequest loginRequest) {
        User user = userRepository.findByNickname(loginRequest.getNickname())
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Błędny login lub hasło");
        }

        CustomUserDetails userDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        String token = jwtUtil.generateToken(userDetails);

        return new LoginResponse(token);
    }


    public UserDTO updateProfilePicture(UUID userId, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony"));

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("Plik awatara nie może być pusty");
        }

        String contentType = multipartFile.getContentType();
        if (contentType == null || !List.of("image/jpeg", "image/png", "image/gif").contains(contentType)) {
            throw new IllegalArgumentException("Niedozwolony typ pliku: " + contentType);
        }

        user.setProfilePicture(multipartFile.getBytes());
        User saved = userRepository.save(user);

        return userMapper.toDTO(saved);
    }

    public UserDTO updateDescription(UUID userId, DescriptionProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie istnieje"));

        userMapper.updateDescriptionFromDto(dto, user);
        User saved = userRepository.save(user);

        return userMapper.toDTO(saved);
    }

    public UserDTO updateUserProfile(UUID userId, UserProfileUpdateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie istnieje"));

        userMapper.updateUserFromDto(dto, user);
        User updated = userRepository.save(user);

        return userMapper.toDTO(updated);
    }

}
