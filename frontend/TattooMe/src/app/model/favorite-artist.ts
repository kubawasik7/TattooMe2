import { UUID } from "crypto";

export interface FavoriteArtist {
    artistId: UUID;
    username: string;
    description: string;
}
