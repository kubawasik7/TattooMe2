package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.user.DescriptionProfileDTO;
import TattooMe.TattooMe.dto.user.UserDTO;
import TattooMe.TattooMe.dto.user.UserProfileUpdateDTO;
import TattooMe.TattooMe.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserDTO>> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserByIdWithAvgRating(id));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllArtistsWithRating(@RequestParam(value = "role", required = false) String role) {
        return ResponseEntity.ok(userService.getAllUsersWithAvgRating(role));
    }

    @GetMapping("/top")
    public ResponseEntity<List<UserDTO>> getTop5Artists() {
        List<UserDTO> topUsers = userService.getTop5Artists();
        return ResponseEntity.ok(topUsers);
    }

    @PostMapping(
            value = "/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadAvatar(@RequestPart("avatar") MultipartFile avatar,
                                          @AuthenticationPrincipal CustomUserDetails principal) throws IOException {
        userService.updateProfilePicture(principal.getId(), avatar);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/description")
    public ResponseEntity<UserDTO> updateDescription(@RequestBody DescriptionProfileDTO dto,
                                                     @AuthenticationPrincipal CustomUserDetails user) {
        UserDTO updated = userService.updateDescription(user.getId(), dto);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/userProfile")
    public ResponseEntity<UserDTO> updateUserProfile(@RequestBody @Valid UserProfileUpdateDTO dto,
                                                     @AuthenticationPrincipal CustomUserDetails user) {
        UserDTO updated = userService.updateUserProfile(user.getId(), dto);
        return ResponseEntity.ok(updated);
    }
}
