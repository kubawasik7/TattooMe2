package TattooMe.TattooMe.service;

import TattooMe.TattooMe.dto.favoriteArtist.FavoriteArtistDTO;
import TattooMe.TattooMe.dto.user.UserDTO;
import TattooMe.TattooMe.entity.FavoriteArtist;
import TattooMe.TattooMe.entity.User;
import TattooMe.TattooMe.mapper.FavoriteArtistMapper;
import TattooMe.TattooMe.repository.FavoriteArtistRepository;
import TattooMe.TattooMe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    private UserService userService;

    public List<UserDTO> getFavoriteArtists(UUID userId) {
        List<UUID> artistIds = favoriteArtistRepository
                .findAllByUser_Id(userId)
                .stream()
                .map(f -> f.getArtist().getId())
                .toList();

        List<UserDTO> result = new ArrayList<>();

        for (UUID artistId : artistIds) {
            userService.getUserByIdWithAvgRating(artistId)
                    .ifPresent(result::add);
        }

        return result;
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