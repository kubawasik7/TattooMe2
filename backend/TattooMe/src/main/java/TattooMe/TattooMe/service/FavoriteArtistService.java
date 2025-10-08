package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.FavoriteArtistDTO;
import TattooMe.TattooMe.dto.OfferDTO;
import TattooMe.TattooMe.dto.UserDTO;
import TattooMe.TattooMe.entity.FavoriteArtist;
import TattooMe.TattooMe.entity.TattooArtistOffer;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.repository.FavoriteArtistRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FavoriteArtistService {
    @Autowired
    private FavoriteArtistRepository favoriteArtistRepository;
    @Autowired
    private UserRepository userRepository;

    public List<FavoriteArtistDTO> getFavorites(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Uzytkownik nie znaleziony"));

        return favoriteArtistRepository.findAllByUser_Id(userId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public FavoriteArtistDTO addFavorite(UUID artistId, UUID userId) {
        boolean exists = favoriteArtistRepository.existsByUser_IdAndArtist_Id(userId, artistId);
        if (exists) throw new IllegalStateException("Artysta juz jest dodany do ulubionych");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Uzytkownik nie znaleziony"));

        User artist = userRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artysta nie znaleziony"));

        FavoriteArtist favorite = new FavoriteArtist();
        favorite.setUser(user);
        favorite.setArtist(artist);

        favoriteArtistRepository.save(favorite);

        return toDto(favorite);
    }

    @Transactional
    public void removeFavorite(UUID userId, UUID artistId) {
        favoriteArtistRepository.deleteByUser_IdAndArtist_Id(userId, artistId);
    }

    private FavoriteArtistDTO toDto(FavoriteArtist favorite) {
        User artist = favorite.getArtist();
        return new FavoriteArtistDTO(
                artist.getId(),
                artist.getNickname(),
                artist.getDescription()
        );
    }
}