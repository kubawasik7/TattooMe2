package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.CreateStudioDTO;
import TattooMe.TattooMe.dto.TattooStudioDTO;
import TattooMe.TattooMe.service.TattooStudioService;
import lombok.RequiredArgsConstructor;
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
    @GetMapping
    public List<TattooStudioDTO> getStudios(){
        return studioService.getAllStudios();
    }

    @PostMapping
    public ResponseEntity<UUID> createStudio(@RequestBody CreateStudioDTO dto,
                                             @AuthenticationPrincipal CustomUserDetails user) {
        UUID studioId = studioService.createStudio(dto, user.getId());
        return ResponseEntity.ok(studioId);
    }
}