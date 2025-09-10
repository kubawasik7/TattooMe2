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
    @GetMapping("/active")
    public ResponseEntity<List<VisitDTO>> getActiveVisits(@AuthenticationPrincipal CustomUserDetails user) {
        List<VisitDTO> visits = visitService.getActiveVisits(user.getId());
        return ResponseEntity.ok(visits);
    }
    @GetMapping("/past")
    public ResponseEntity<List<VisitDTO>> getPastVisits(@AuthenticationPrincipal CustomUserDetails user) {
        List<VisitDTO> visits = visitService.getPastVisits(user.getId());
        return ResponseEntity.ok(visits);
    }
    @GetMapping("/cancelled")
    public ResponseEntity<List<VisitDTO>> getCancelledVisits(@AuthenticationPrincipal CustomUserDetails user) {
        List<VisitDTO> visits = visitService.getCancelledVisits(user.getId());
        return ResponseEntity.ok(visits);
    }
}