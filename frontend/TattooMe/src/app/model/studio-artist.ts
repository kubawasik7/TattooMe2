import { UUID } from "crypto";
import { Featured } from "./featured";
import { StudioRole } from "./studio-role";

export interface StudioArtist {
    id: UUID;
    nickname: string;
    email: string;
    name: string;
    surname: string;
    description: string;
    profilePicture: string | null;
    averageRate: number;
    reviewsCount: number;
    featuredPictures: Featured[];
    studioRole: StudioRole;
    studioId: UUID;
    showSlots?: boolean;
}
