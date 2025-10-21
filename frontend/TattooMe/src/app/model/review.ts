import { ReviewAnswer } from "./review-answer";

export interface Review {
  id: string;
  rate: number;                  // odpowiada rating z backendu
  content: string;
  createdAt: string;
  clientName: string;            // zamiast authorNickname
  artistName: string;
  answers: ReviewAnswer[];
}