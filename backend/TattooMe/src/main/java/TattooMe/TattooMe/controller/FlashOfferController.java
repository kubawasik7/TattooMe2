package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.dto.flashOffer.FlashOfferDTO;
import TattooMe.TattooMe.service.FlashOfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/flash-offer")
@RequiredArgsConstructor
@CrossOrigin
public class FlashOfferController {
    @Autowired
    private FlashOfferService flashOfferService;

    @GetMapping("/artist-offer/{tattooArtistOfferId}")
    public ResponseEntity<List<FlashOfferDTO>> getOffers(@PathVariable UUID tattooArtistOfferId) {
        return ResponseEntity.ok(flashOfferService.getOffersByArtistOffer(tattooArtistOfferId));
    }

    @PostMapping
    public ResponseEntity<FlashOfferDTO> create(@RequestBody FlashOfferDTO dto) {
        return ResponseEntity.ok(flashOfferService.createFlashOffer(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        flashOfferService.deleteFlashOffer(id);
        return ResponseEntity.noContent().build();
    }
}