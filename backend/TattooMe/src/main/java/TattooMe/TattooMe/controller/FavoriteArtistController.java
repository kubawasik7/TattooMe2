package TattooMe.TattooMe.controller;


import TattooMe.TattooMe.Security.CustomUserDetails;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.service.FavoriteArtistService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:4200")
public class FavoriteArtistController {
    private final FavoriteArtistService favoriteArtistService;

    public FavoriteArtistController(FavoriteArtistService service) {
        this.favoriteArtistService = service;
    }

    @GetMapping
    public List<User> getFavorites(@AuthenticationPrincipal CustomUserDetails user) {
        return favoriteArtistService.getFavorites(user.getId());
    }

    @PostMapping("/{artistId}")
    public void addFavorite(@PathVariable UUID artistId,
                            @AuthenticationPrincipal CustomUserDetails user) {
        favoriteArtistService.addFavorite(user.getId(), artistId);
    }

    @DeleteMapping("/{artistId}")
    public void removeFavorite(@PathVariable UUID artistId,
                               @AuthenticationPrincipal CustomUserDetails user) {
        favoriteArtistService.removeFavorite(user.getId(), artistId);
    }
}