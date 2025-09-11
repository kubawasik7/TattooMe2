import { ReviewAnswer } from "./review-answer";

export interface Review {
  id: string;
  rate: number;
  content: string;
  createdAt: string;
  authorNickname: string;
  targetId?: string;
  tattooStudioId?: string;
  answers: ReviewAnswer[];
}