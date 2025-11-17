import { UUID } from "crypto";

export interface TattooStudio {
  id: UUID,
  name: string;
  city: string;
  street: string;
  streetNumber: string;
  postalCode: string;
  profilePicture: string | null;
  description: string;
  ownerNickname: string;
}
