package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.NewVisitDTO;
import TattooMe.TattooMe.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}