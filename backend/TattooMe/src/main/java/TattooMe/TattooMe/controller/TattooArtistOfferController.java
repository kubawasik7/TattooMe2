package TattooMe.TattooMe.controller;

import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.CreateOfferDTO;
import TattooMe.TattooMe.dto.OfferDTO;
import TattooMe.TattooMe.service.TattooArtistOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private TattooArtistOfferService service;

    @GetMapping
    public List<OfferDTO> list(@AuthenticationPrincipal CustomUserDetails user) {
        return service.getOffers(user.getId());
    }

    @PostMapping
    public OfferDTO create(@AuthenticationPrincipal CustomUserDetails user,
                           @RequestBody CreateOfferDTO dto) {
        return service.createOffer(user.getId(), dto);
    }

    @PutMapping("/{id}")
    public OfferDTO update(@AuthenticationPrincipal CustomUserDetails user,
                           @PathVariable UUID id,
                           @RequestBody CreateOfferDTO dto) throws AccessDeniedException {
        return service.updateOffer(user.getId(), id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal CustomUserDetails user,
                       @PathVariable UUID id) throws AccessDeniedException {
        service.deleteOffer(user.getId(), id);
    }

}
