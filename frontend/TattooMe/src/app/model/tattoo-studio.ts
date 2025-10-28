import { UUID } from "crypto";

export interface TattooStudio {
  id: UUID,
  name: string;
  city: string;
  street: string;
  streetNumber: string;
  postalCode: string;
  ownerNickname: string;
}
