package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.dto.flashOffer.FlashOfferDTO;
import TattooMe.TattooMe.service.FlashOfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/flash-offer")
@CrossOrigin(origins = "http://localhost:4200")
public class FlashOfferController {
    @Autowired
    private FlashOfferService flashOfferService;

    @GetMapping("/artist-offer/{tattooArtistOfferId}")
    public ResponseEntity<List<FlashOfferDTO>> getOffers(@PathVariable UUID tattooArtistOfferId) {
        return ResponseEntity.ok(flashOfferService.getOffersByArtistOffer(tattooArtistOfferId));
    }

    @PostMapping
    public ResponseEntity<FlashOfferDTO> createOffer(@RequestBody @Valid FlashOfferDTO dto) {
        return ResponseEntity.ok(flashOfferService.createFlashOffer(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable UUID id) {
        flashOfferService.deleteFlashOffer(id);
        return ResponseEntity.noContent().build();
    }
}