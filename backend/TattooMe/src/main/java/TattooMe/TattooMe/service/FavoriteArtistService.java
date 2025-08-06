package TattooMe.TattooMe.service;

import TattooMe.TattooMe.entity.FavoriteArtist;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.FavoriteArtistRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FavoriteArtistService {

    private final FavoriteArtistRepository favoriteArtistRepository;
    private final UserRepository userRepository;

    public FavoriteArtistService(FavoriteArtistRepository favoriteArtistRepository, UserRepository userRepository) {
        this.favoriteArtistRepository = favoriteArtistRepository;
        this.userRepository = userRepository;
    }

    public List<User> getFavorites(UUID userId) {
        return favoriteArtistRepository.findAllByUser_Id(userId).stream()
                .map(FavoriteArtist::getArtist)
                .toList();
    }

    public void addFavorite(UUID userId, UUID artistId) {
        boolean exists = favoriteArtistRepository.existsByUser_IdAndArtist_Id(userId, artistId);
        if (exists) return;

        FavoriteArtist fav = new FavoriteArtist();
        fav.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found")));
        fav.setArtist(userRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artist not found")));

        favoriteArtistRepository.save(fav);
    }
    @Transactional
    public void removeFavorite(UUID userId, UUID artistId) {
        favoriteArtistRepository.deleteByUser_IdAndArtist_Id(userId, artistId);
    }
}