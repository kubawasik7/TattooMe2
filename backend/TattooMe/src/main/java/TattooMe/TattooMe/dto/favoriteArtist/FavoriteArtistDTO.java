package TattooMe.TattooMe.dto.favoriteArtist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FavoriteArtistDTO {
    private UUID artistId;
    private String username;
    private String description;
}
