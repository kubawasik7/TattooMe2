import { UUID } from "crypto";
import { Featured } from "./featured";

export interface User {
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
}
