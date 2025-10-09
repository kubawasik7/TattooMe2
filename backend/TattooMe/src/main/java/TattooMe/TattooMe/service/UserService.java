package TattooMe.TattooMe.service;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.Security.JwtUtil;
import TattooMe.TattooMe.dto.*;
import TattooMe.TattooMe.entity.User;
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
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    public List<UserDTO> getUsersByRole(String role) {
        return userRepository.findAllByRole(role).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nie znaleziono użytkownika"));

        return toDto(user);
    }

    public RegisterResponse registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByNickname(registerRequest.getNickname()).isPresent()) {
            throw new RuntimeException("Nazwa uzytkownika jest juz zajeta");
        }

        User user = new User();
        user.setRole(registerRequest.getRole());
        user.setNickname(registerRequest.getNickname());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        User saved = userRepository.save(user);

        return new RegisterResponse(
                saved.getNickname(),
                saved.getEmail(),
                saved.getRole()
        );
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


    public void updateProfilePicture(UUID userId, MultipartFile multipartFile) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony"));

        if (multipartFile.isEmpty()) {
            throw new IllegalArgumentException("Plik awatara nie może być pusty");
        }
        String contentType = multipartFile.getContentType();
        if (contentType == null ||
                !List.of("image/jpeg", "image/png", "image/gif").contains(contentType)) {
            throw new IllegalArgumentException("Niedozwolony typ pliku: " + contentType);
        }
        byte[] bytes = multipartFile.getBytes();
        user.setProfilePicture(bytes);
    }

    public User updateDescription(UUID userId, String newDesc) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));
        user.setDescription(newDesc);
        return userRepository.save(user);
    }

    public User updateUserProfile(UUID userId, UserDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Uzytkownik nie istnieje"));
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());
        return userRepository.save(user);
    }

    private UserDTO toDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNickname(user.getNickname());
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setEmail(user.getEmail());
        userDTO.setDescription(user.getDescription());
        if (user.getProfilePicture() != null) {
            userDTO.setProfilePicture(Base64.getEncoder().encodeToString(user.getProfilePicture()));
        }
        return userDTO;
    }
}
