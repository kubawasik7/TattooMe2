import { UUID } from "crypto";

export interface User {
      id: UUID;
      nickname: string;
      email: string;
      name: string;
      surname: string;
      description: string;
      profilePicture: string | null;
}
