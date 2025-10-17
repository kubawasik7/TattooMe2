package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.tattooStyle.TattooStyleDTO;
import TattooMe.TattooMe.service.WorkStyleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/styles")
@CrossOrigin(origins = "http://localhost:4200")
public class WorkStyleController {
    @Autowired
    private WorkStyleService service;

    @GetMapping("/all")
    public ResponseEntity<List<TattooStyleDTO>> getAll() {
        return ResponseEntity.ok(service.getAllStyles());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<TattooStyleDTO>> getForUser(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getUserStyles(id));
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<List<TattooStyleDTO>> saveForUser(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody List<UUID> styleIds) {
        List<TattooStyleDTO> tattooStyles = service.saveUserStyles(user.getId(), styleIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(tattooStyles);
    }

}