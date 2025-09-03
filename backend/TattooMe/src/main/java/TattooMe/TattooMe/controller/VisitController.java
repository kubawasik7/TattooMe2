package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.NewVisitDTO;
import TattooMe.TattooMe.dto.VisitDTO;
import TattooMe.TattooMe.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/visits")
@RequiredArgsConstructor
public class VisitController {
    private final VisitService visitService;
    @PostMapping
    public ResponseEntity<Void> createVisit(@AuthenticationPrincipal CustomUserDetails user, @RequestBody NewVisitDTO newVisitDTO) {
        visitService.createVisit(user.getId(), newVisitDTO);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/my")
    public ResponseEntity<List<VisitDTO>> getMyVisits(@AuthenticationPrincipal CustomUserDetails user) {
        List<VisitDTO> visits = visitService.getMyVisits(user.getId(), user.getRole());
        return ResponseEntity.ok(visits);
    }
}