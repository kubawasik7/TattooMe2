package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.DescriptionProfileDTO;
import TattooMe.TattooMe.dto.UserDTO;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllArtists(@RequestParam(value = "role", required = false) String role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
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
    public ResponseEntity<User> updateDescription(@RequestBody DescriptionProfileDTO dto,
                                                  @AuthenticationPrincipal CustomUserDetails principal) {
        User updated = userService.updateDescription(principal.getId(), dto.getDescription());
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/userProfile")
    public ResponseEntity<User> updateUserProfile(@RequestBody UserDTO dto,
                                                  @AuthenticationPrincipal CustomUserDetails principal) {
        User updated = userService.updateUserProfile(principal.getId(), dto);
        return ResponseEntity.ok(updated);
    }
}
