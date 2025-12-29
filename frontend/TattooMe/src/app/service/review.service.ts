import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Review } from '../model/review';
import { ReviewAnswer } from '../model/review-answer';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private url = 'http://localhost:8080/api/reviews';

  constructor(private http: HttpClient) { }

addReview(visitId: string, rate: number, content: string) {
  console.log("Sending review, token:", localStorage.getItem('token'), 'AAAAAAAAAAAAAA',visitId);
  return this.http.post<Review>(
    `${this.url}/${visitId}`,
    { visitId, rate, content },
    { headers: { Authorization: `Bearer ${localStorage.getItem('token')}` } }
  );
}


  addAnswer(reviewId: string, content: string): Observable<ReviewAnswer> {
    return this.http.post<ReviewAnswer>(`${this.url}/answers`, {
      reviewId,
      content
    });
  }

  getReviewsForArtist(artistId: string): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.url}/artist/${artistId}`);
  }

  getReviewsForStudio(studioId: string): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.url}/studio/${studioId}`);
  }

  getReviewForVisit(visitId: string): Observable<Review | null> {
    return this.http.get<Review>(`${this.url}/visit/${visitId}`);
  }
}