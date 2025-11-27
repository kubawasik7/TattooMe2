import { ReviewAnswer } from "./review-answer";

export interface Review {
  id: string;
  rate: number;
  content: string;
  createdAt: string;
  clientName: string;
  artistName: string;
  answers: ReviewAnswer[];
}