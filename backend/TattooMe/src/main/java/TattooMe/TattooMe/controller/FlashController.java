package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.FlashDTO;
import TattooMe.TattooMe.entity.Flash;
import TattooMe.TattooMe.service.FlashService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/flashes")
@CrossOrigin(origins = "http://localhost:4200")
public class FlashController {
    private final FlashService flashService;
    public FlashController(FlashService flashService) {
        this.flashService = flashService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> uploadFlash(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") FlashDTO data
    ) throws IOException {
        flashService.save(principal.getId(), file, data);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{userId}")
    public List<Flash> getUserFlashes(@PathVariable UUID userId) {
        return flashService.getUserFlashes(userId);
    }
}
