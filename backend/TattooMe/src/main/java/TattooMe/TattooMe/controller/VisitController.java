package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.visit.NewVisitDTO;
import TattooMe.TattooMe.dto.visit.VisitDTO;
import TattooMe.TattooMe.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/visits")
@CrossOrigin(origins = "http://localhost:4200")
public class VisitController {
    @Autowired
    private VisitService visitService;

    @PostMapping
    public ResponseEntity<Void> createVisit(@AuthenticationPrincipal CustomUserDetails user, @RequestBody NewVisitDTO newVisitDTO) {
        visitService.createVisit(user.getId(), newVisitDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDTO> getVisitDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(visitService.getVisitDetails(id));
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

    @GetMapping("/artist/active")
    @PreAuthorize("hasRole('tattoo_artist')")
    public ResponseEntity<List<VisitDTO>> getActiveVisitsAsArtist(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(visitService.getActiveVisitsAsArtist(user.getId()));
    }

    @GetMapping("/artist/past")
    @PreAuthorize("hasRole('tattoo_artist')")
    public ResponseEntity<List<VisitDTO>> getPastVisitsAsArtist(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(visitService.getPastVisitsAsArtist(user.getId()));
    }

    @GetMapping("/artist/cancelled")
    @PreAuthorize("hasRole('tattoo_artist')")
    public ResponseEntity<List<VisitDTO>> getCancelledVisitsAsArtist(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(visitService.getCancelledVisitsAsArtist(user.getId()));
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasRole('tattoo_artist')")
    public ResponseEntity<Void> confrimVisit(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails user) {
        boolean approved = visitService.confirmVisit(id, user.getId());
        return approved ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('tattoo_artist')")
    public ResponseEntity<Void> cancelVisit(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails user) {
        boolean cancelled = visitService.cancelVisitAsArtist(id, user.getId());
        if (cancelled) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PatchMapping("/{id}/cancel/client")
    @PreAuthorize("hasRole('client')")
    public ResponseEntity<Void> cancelVisitAsClient(@PathVariable UUID id, @AuthenticationPrincipal CustomUserDetails user) {

        boolean cancelled = visitService.cancelVisitAsClient(id, user.getId());
        return cancelled ? ResponseEntity.ok().build() : ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}