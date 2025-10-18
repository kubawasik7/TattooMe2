package TattooMe.TattooMe.mapper;

import TattooMe.TattooMe.dto.favoriteArtist.FavoriteArtistDTO;
import TattooMe.TattooMe.entity.FavoriteArtist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FavoriteArtistMapper {
    @Mapping(source = "artist.id", target = "artistId")
    @Mapping(source = "artist.nickname", target = "username")
    @Mapping(source = "artist.description", target = "description")
    FavoriteArtistDTO toDTO(FavoriteArtist favorite);
}