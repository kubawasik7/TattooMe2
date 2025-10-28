package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.tattooStudio.CreateStudioDTO;
import TattooMe.TattooMe.dto.tattooStudio.TattooStudioDTO;
import TattooMe.TattooMe.dto.user.UserDTO;
import TattooMe.TattooMe.service.TattooStudioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/studios")
@RequiredArgsConstructor
public class TattooStudioController {

    private final TattooStudioService studioService;

    @GetMapping("/{id}")
    public ResponseEntity<TattooStudioDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(studioService.getTattooStudioById(id));
    }

    @GetMapping
    public List<TattooStudioDTO> getStudios() {
        return studioService.getAllStudios();
    }

    @GetMapping("/{studioId}/users")
    public ResponseEntity<List<UserDTO>> getUsersByStudio(@PathVariable UUID studioId) {
        return ResponseEntity.ok(studioService.getUsersByStudioIdWithAvgRatingAndFeatured(studioId));
    }

    @PostMapping("/{studioId}/users/by-nickname")
    public ResponseEntity<Void> addUserToStudioByNickname(
            @PathVariable UUID studioId,
            @RequestParam String nickname) {
        studioService.addUserToStudio(studioId, nickname);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{studioId}/users/{userId}")
    public ResponseEntity<Void> removeUserFromStudio(@PathVariable UUID studioId, @PathVariable UUID userId) {
        studioService.removeUserFromStudio(studioId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<TattooStudioDTO> createStudio(@Valid @RequestBody CreateStudioDTO dto,
                                                        @AuthenticationPrincipal CustomUserDetails user) {
        TattooStudioDTO studio = studioService.createStudio(dto, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(studio);
    }
}