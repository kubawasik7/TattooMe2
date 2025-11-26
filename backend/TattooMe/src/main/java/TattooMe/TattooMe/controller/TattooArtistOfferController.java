package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.offer.CreateOfferDTO;
import TattooMe.TattooMe.dto.offer.OfferDTO;
import TattooMe.TattooMe.service.TattooArtistOfferService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/offers")
@CrossOrigin(origins = "http://localhost:4200")
public class TattooArtistOfferController {
    @Autowired
    private TattooArtistOfferService artistOfferService;

    @GetMapping("/{id}")
    public ResponseEntity<List<OfferDTO>> getTattooArtistOffers(@PathVariable UUID id) {
        return ResponseEntity.ok(artistOfferService.getOffers(id));
    }

    @PostMapping
    public ResponseEntity<OfferDTO> createTattooArtistOffer(@AuthenticationPrincipal CustomUserDetails user,
                                           @RequestBody @Valid CreateOfferDTO dto) {
        OfferDTO offer = artistOfferService.createOffer(user.getId(), dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(offer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OfferDTO> updateTattooArtistOffer(@AuthenticationPrincipal CustomUserDetails user,
                                           @PathVariable UUID id,
                                           @RequestBody @Valid CreateOfferDTO dto) throws AccessDeniedException {
        return ResponseEntity.ok(artistOfferService.updateOffer(user.getId(), id, dto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteTattooArtistOffer(@AuthenticationPrincipal CustomUserDetails user,
                                       @PathVariable UUID id) throws AccessDeniedException {
        artistOfferService.deleteOffer(user.getId(), id);
        return ResponseEntity.noContent().build();
    }
}
