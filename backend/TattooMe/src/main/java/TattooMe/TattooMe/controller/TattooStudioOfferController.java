package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.dto.offer.CreateOfferDTO;
import TattooMe.TattooMe.dto.offer.OfferDTO;
import TattooMe.TattooMe.service.TattooStudioOfferService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/studio-offers")
@CrossOrigin(origins = "http://localhost:4200")
public class TattooStudioOfferController {

    @Autowired
    private TattooStudioOfferService studioOfferService;

    @GetMapping("/{studioId}")
    public ResponseEntity<List<OfferDTO>> getTattooStudioOffers(@PathVariable UUID studioId) {
        return ResponseEntity.ok(studioOfferService.getStudioOffers(studioId));
    }

    @GetMapping("/{studioId}/combined")
    public ResponseEntity<List<OfferDTO>> getCombinedOffers(@PathVariable UUID studioId) {
        List<OfferDTO> offers = studioOfferService.getAllOffersForStudio(studioId);
        return ResponseEntity.ok(offers);
    }

    @PostMapping("/{studioId}")
    public ResponseEntity<OfferDTO> createTattooStudioOffer(@PathVariable UUID studioId,
                                                            @RequestBody @Valid CreateOfferDTO dto) {

        OfferDTO offer = studioOfferService.createOffer(studioId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(offer);
    }

    @PutMapping("/{studioId}/{offerId}")
    public ResponseEntity<OfferDTO> updateTattooStudioOffer(@PathVariable UUID studioId,
                                           @PathVariable UUID offerId,
                                           @RequestBody @Valid CreateOfferDTO dto)
            throws AccessDeniedException {

        return ResponseEntity.ok(studioOfferService.updateOffer(studioId, offerId, dto));
    }

    @DeleteMapping("/{studioId}/{offerId}")
    public ResponseEntity<Void> deleteTattooStudioOffer(@PathVariable UUID studioId,
                                       @PathVariable UUID offerId) {
        studioOfferService.deleteOffer(studioId, offerId);
        return ResponseEntity.noContent().build();
    }
}
