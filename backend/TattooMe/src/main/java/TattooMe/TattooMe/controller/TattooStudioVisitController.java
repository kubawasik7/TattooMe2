package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.TattooStudioVisit.TattooStudioVisitRequest;
import TattooMe.TattooMe.dto.TattooStudioVisit.TattooStudioVisitResponse;
import TattooMe.TattooMe.entity.TattooStudioVisit;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.service.TattooStudioVisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/studio-visits")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TattooStudioVisitController {

    private final TattooStudioVisitService service;

    @PostMapping("/{studioId}")
    public ResponseEntity<TattooStudioVisitResponse> createVisit(
            @PathVariable UUID studioId,
            @Valid @RequestBody TattooStudioVisitRequest request,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return ResponseEntity.ok(service.createVisit(studioId, request, user.getId()));
    }

    @GetMapping("/studio/active")
    public ResponseEntity<List<TattooStudioVisitResponse>> getActive() {
        return ResponseEntity.ok(service.getActive());
    }

    @GetMapping("/studio/past")
    public ResponseEntity<List<TattooStudioVisitResponse>> getPast() {
        return ResponseEntity.ok(service.getPast());
    }

    @GetMapping("/studio/cancelled")
    public ResponseEntity<List<TattooStudioVisitResponse>> getCancelled() {
        return ResponseEntity.ok(service.getCancelled());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TattooStudioVisitResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PatchMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmVisit(@PathVariable UUID id) {
        service.confirmVisit(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/cancel/studio")
    public ResponseEntity<Void> cancelVisitAsStudio(@PathVariable UUID id) {
        service.cancelVisitAsStudio(id);
        return ResponseEntity.ok().build();
    }
}
