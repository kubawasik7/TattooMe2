import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Review } from '../model/review';
import { ReviewAnswer } from '../model/review-answer';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private apiUrl = 'http://localhost:8080/api/reviews';

  constructor(private http: HttpClient) {}

  addReview(visitId: string, rate: number, content?: string): Observable<Review> {
    const params: any = { rate };
    if (content) params.content = content;
    return this.http.post<Review>(`${this.apiUrl}/${visitId}`, null, { params });
  }

  addAnswer(reviewId: string, content: string): Observable<ReviewAnswer> {
    return this.http.post<ReviewAnswer>(`${this.apiUrl}/${reviewId}/answers`, null, {
      params: { content }
    });
  }

  getReviewsForArtist(artistId: string): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/artist/${artistId}`);
  }

  getReviewsForStudio(studioId: string): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.apiUrl}/studio/${studioId}`);
  }
  getReviewForVisit(visitId: string): Observable<Review | null> {
  return this.http.get<Review>(`${this.apiUrl}/visit/${visitId}`);
}

}