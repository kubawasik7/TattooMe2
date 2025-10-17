package TattooMe.TattooMe.controller;


import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.dto.favoriteArtist.FavoriteArtistDTO;
import TattooMe.TattooMe.service.FavoriteArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:4200")
public class FavoriteArtistController {
    @Autowired
    private FavoriteArtistService favoriteArtistService;

    @GetMapping
    public ResponseEntity<List<FavoriteArtistDTO>> getFavorites(@AuthenticationPrincipal CustomUserDetails user) {
        List<FavoriteArtistDTO> favorites = favoriteArtistService.getFavorites(user.getId());
        return ResponseEntity.ok(favorites);
    }

    @PostMapping("/{artistId}")
    public ResponseEntity<FavoriteArtistDTO> addFavorite(@PathVariable UUID artistId,
                                                         @AuthenticationPrincipal CustomUserDetails user) {
        FavoriteArtistDTO favorite = favoriteArtistService.addFavorite(artistId, user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(favorite);
    }

    @DeleteMapping("/{artistId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable UUID artistId,
                                               @AuthenticationPrincipal CustomUserDetails user) {
        favoriteArtistService.removeFavorite(user.getId(), artistId);
        return ResponseEntity.noContent().build();
    }
}