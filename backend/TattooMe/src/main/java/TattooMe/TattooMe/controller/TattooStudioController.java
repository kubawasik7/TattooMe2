package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.tattooStudio.CreateStudioDTO;
import TattooMe.TattooMe.dto.tattooStudio.TattooStudioDTO;
import TattooMe.TattooMe.service.TattooStudioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/studios")
@RequiredArgsConstructor
public class TattooStudioController {

    private final TattooStudioService studioService;

    @GetMapping
    public List<TattooStudioDTO> getStudios() {
        return studioService.getAllStudios();
    }

    @PostMapping
    public ResponseEntity<TattooStudioDTO> createStudio(@Valid @RequestBody CreateStudioDTO dto,
                                                        @AuthenticationPrincipal CustomUserDetails user) {
        TattooStudioDTO studio = studioService.createStudio(dto, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(studio);
    }
}