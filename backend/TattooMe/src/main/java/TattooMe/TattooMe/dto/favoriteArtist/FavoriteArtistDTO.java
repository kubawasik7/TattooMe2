package TattooMe.TattooMe.dto.favoriteArtist;

import TattooMe.TattooMe.dto.Featured.FeaturedDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class FavoriteArtistDTO {
    private UUID artistId;
    private String username;
    private String description;
    private byte[] profilePicture;
    private Double averageRate;
    private Long reviewsCount;
    private List<FeaturedDTO> featuredPictures;
}
