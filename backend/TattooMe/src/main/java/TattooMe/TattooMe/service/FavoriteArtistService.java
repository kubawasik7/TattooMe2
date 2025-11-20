package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.favoriteArtist.FavoriteArtistDTO;
import TattooMe.TattooMe.entity.FavoriteArtist;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.FavoriteArtistMapper;
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
    @Autowired
    private FavoriteArtistMapper favoriteArtistMapper;

    public List<FavoriteArtistDTO> getFavorites(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony"));

        return favoriteArtistRepository.findAllByUser_Id(userId)
                .stream()
                .map(favoriteArtistMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public FavoriteArtistDTO addFavorite(UUID artistId, UUID userId) {
        boolean exists = favoriteArtistRepository.existsByUser_IdAndArtist_Id(userId, artistId);
        if (exists) {
            throw new IllegalStateException("Artysta już jest dodany do ulubionych");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Użytkownik nie znaleziony"));

        User artist = userRepository.findById(artistId)
                .orElseThrow(() -> new EntityNotFoundException("Artysta nie znaleziony"));

        FavoriteArtist favorite = new FavoriteArtist();
        favorite.setUser(user);
        favorite.setArtist(artist);
        favoriteArtistRepository.save(favorite);

        return favoriteArtistMapper.toDTO(favorite);
    }

    @Transactional
    public void removeFavorite(UUID userId, UUID artistId) {
        if (!favoriteArtistRepository.existsByUser_IdAndArtist_Id(userId, artistId)) {
            throw new EntityNotFoundException("Ulubiony artysta nie istnieje");
        }
        favoriteArtistRepository.deleteByUser_IdAndArtist_Id(userId, artistId);
    }
}