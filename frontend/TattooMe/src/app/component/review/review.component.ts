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
  @Input() isOwner: boolean = false;



  reviews: Review[] = [];

  rate = 0;
  content = '';
  showForm = false;
  submitted = false;

  answerContent: { [key: string]: string } = {};

  constructor(private reviewService: ReviewService, public authService: AuthService) { }

  existingReview: Review | null = null;


  ngOnInit(): void {
    if (this.visitId) {
      this.reviewService.getReviewForVisit(this.visitId).subscribe({
        next: r => this.existingReview = r,
        error: err => {

          console.warn('Brak opinii dla wizyty lub brak dostępu:', err);
          this.existingReview = null;
        }
      });
    }

    if (this.artistId) {
      this.reviewService.getReviewsForArtist(this.artistId).subscribe({
        next: r => this.reviews = r,
        error: err => {
          console.warn('Brak opinii dla artysty lub brak dostępu:', err);
          this.reviews = [];
        }
      });
    }

    if (this.studioId) {
      this.reviewService.getReviewsForStudio(this.studioId).subscribe({
        next: r => this.reviews = r,
        error: err => {
          console.warn('Brak opinii dla studia lub brak dostępu:', err);
          this.reviews = [];
        }
      });
    }
  }


  canAddReview(): boolean {
    return (
      !!this.visitId &&
      (this.visitStatus === 'ZATWIERDZONA' || this.visitStatus === 'ANULOWANA')
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