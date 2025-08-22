package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.CreateScheduleDTO;
import TattooMe.TattooMe.dto.ScheduleDTO;
import TattooMe.TattooMe.service.ArtistDateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/schedule")
@CrossOrigin(origins = "http://localhost:4200")
public class ArtistDateController {
    @Autowired
    private ArtistDateService artistDateService;

    @GetMapping
    public List<ScheduleDTO> getArtistDates(@AuthenticationPrincipal CustomUserDetails user){
        return artistDateService.listSlots(user.getId());
    }
    @GetMapping("/available")
    public List<ScheduleDTO> getAvailableSlots(@RequestParam UUID artistId) {
        return artistDateService.getAvailableByArtist(artistId);
    }
    @PostMapping
    public ResponseEntity<ScheduleDTO> create(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody CreateScheduleDTO dto) {
        ScheduleDTO created = artistDateService.createSlot(user.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable UUID id) throws AccessDeniedException {
        artistDateService.deleteSlot(user.getId(), id);
    }

    @PutMapping("/{id}/toggle")
    public ScheduleDTO toggle(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable UUID id) throws AccessDeniedException {
        return artistDateService.toggleAvailability(user.getId(), id);
    }
}
