import { Component, Input, OnInit } from '@angular/core';
import { Review } from '../../model/review';
import { ReviewService } from '../../service/review.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-review',
  standalone: false,
  templateUrl: './review.component.html',
  styleUrl: './review.component.css'
})
export class ReviewsComponent implements OnInit {
  @Input() artistId?: string;
  @Input() studioId?: string;
  @Input() visitId?: string;     
  @Input() visitStatus?: string;
  @Input() visitDate?: string;

  reviews: Review[] = [];

  rate = 0;
  content = '';
  showForm = false;
  submitted = false;

  answerContent: { [key: string]: string } = {};

  constructor(private reviewService: ReviewService, public authService: AuthService) {}

existingReview: Review | null = null;


ngOnInit(): void {
  if (this.visitId) {
    this.reviewService.getReviewForVisit(this.visitId).subscribe({
      next: r => this.existingReview = r,
      error: () => this.existingReview = null 
    });
  }

  if (this.artistId) {
    this.reviewService.getReviewsForArtist(this.artistId).subscribe(r => this.reviews = r);
  }

  if (this.studioId) {
    this.reviewService.getReviewsForStudio(this.studioId).subscribe(r => this.reviews = r);
  }
}


canAddReview(): boolean {
  return (
    !!this.visitId &&
    this.visitStatus === 'ZATWIERDZONA' &&
    new Date(this.visitDate!).getTime() < Date.now()
  );
}


  setRate(star: number) {
    this.rate = star;
  }

  submitReview() {
    if (!this.visitId || this.rate === 0) return;

    this.reviewService.addReview(this.visitId, this.rate, this.content).subscribe(r => {
      this.reviews.unshift(r);
      this.submitted = true;
      this.showForm = false;
      this.rate = 0;
      this.content = '';
    });
  }

  addAnswer(reviewId: string) {
    if (!this.answerContent[reviewId]) return;
    this.reviewService.addAnswer(reviewId, this.answerContent[reviewId]).subscribe(answer => {
      const review = this.reviews.find(r => r.id === reviewId);
      if (review) {
        review.answers.push(answer);
      }
      this.answerContent[reviewId] = '';
    });
  }
}