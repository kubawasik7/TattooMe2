package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.flash.FlashDTO;
import TattooMe.TattooMe.service.FlashService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private FlashService flashService;

    @GetMapping("/{userId}")
    public ResponseEntity<List<FlashDTO>> getUserFlashes(@PathVariable UUID userId) {
        return ResponseEntity.ok(flashService.getUserFlashes(userId));
    }

    @PostMapping("/upload")
    public ResponseEntity<FlashDTO> uploadFlash(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") @Valid FlashDTO flash
    ) throws IOException {
        FlashDTO flashDTO = flashService.addFlash(user.getId(), file, flash);
        return ResponseEntity.status(HttpStatus.CREATED).body(flashDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlashDTO> updateFlash(@PathVariable UUID id, @RequestBody @Valid FlashDTO dto) {
        FlashDTO updated = flashService.updateFlash(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlash(@PathVariable UUID id) {
        flashService.deleteFlash(id);
        return ResponseEntity.noContent().build();
    }
}
