import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { UserService } from '../../service/user.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ProfileService } from '../../service/profile.service';
import { FavoriteService } from '../../service/favorite.service';
import { AuthService } from '../../service/auth.service';
import { ChatService } from '../../service/chat.service';
import { User } from '../../model/user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NotificationService } from '../../service/notification.service';
import { ReviewService } from '../../service/review.service';
import { Review } from '../../model/review';



@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  user!: User;
  userId!: string;
  authUserId: string | null = null;
  editing = false;
  isOwner = false;
  isLoggedIn = false;
  descriptionForm!: FormGroup;
  averageRate: number = 0;
  reviews: Review[] = [];

  @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  selectedFile: File | null = null;
  previewUrl: string | null = null;
  defaultAvatar = '/pobrane.png';

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private profileService: ProfileService,
    private favoriteService: FavoriteService,
    private authService: AuthService,
    private reviewService: ReviewService,
    private chatService: ChatService,
    private router: Router,
    private fb: FormBuilder,
    private notification: NotificationService
  ) { }


  ngOnInit(): void {
    this.userId = this.route.snapshot.paramMap.get('id')!;

    this.authUserId = this.authService.getUserId();
    this.isLoggedIn = !!this.authUserId;
    this.isOwner = this.authUserId === this.userId;

    this.descriptionForm = this.fb.group({
      description: ['', [Validators.maxLength(1000)]],
    });

    this.userService.getUserById(this.userId).subscribe(user => {
      this.user = user;

      if (user.profilePicture) {
        this.previewUrl = `data:image/png;base64,${user.profilePicture}`;
      } else {
        this.previewUrl = null;
      }

      this.descriptionForm.patchValue({ description: this.user.description || '' });
    });
    this.loadReviews();
  }

  startChat(): void {
    this.chatService.startChat(this.userId).subscribe({
      next: chat => this.router.navigate(['/chat', chat.id]),
      error: err => this.notification.showError("Nie udalo się otworzyc chatu", err)
    });
  }

  goToChats(userId: string) {
    window.location.href = `/chats?receiver=${userId}`;
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;
    this.selectedFile = input.files[0];

    const reader = new FileReader();
    reader.onload = () => this.previewUrl = reader.result as string;
    reader.readAsDataURL(this.selectedFile);
  }

  upload(): void {
    if (!this.selectedFile) return;
    this.profileService.uploadAvatar(this.selectedFile)
      .subscribe({
        next: () => {
          this.notification.showSuccess("Zdjęcie zostało dodane");
          this.selectedFile = null;
        },
        error: err => this.notification.showError("Nie udało się dodać zdjęcia", err)
      });
  }

  //SEKCJA DESCRIPTION
  startEdit(): void {
    this.editing = true;
    setTimeout(() => {
      const textarea = document.querySelector<HTMLTextAreaElement>('textarea');
      if (textarea) this.adjustHeight(textarea);
    });
  }

  cancelEdit(): void {
    this.editing = false;
    this.descriptionForm.patchValue({ description: this.user.description });
  }

  get descriptionControl() {
    return this.descriptionForm.get('description');
  }

  saveDescription(): void {
    if (this.descriptionForm.invalid) return;

    const newDescription = this.descriptionForm.value.description;

    this.profileService.updateDescription(newDescription).subscribe({
      next: updated => {
        this.user.description = updated.description;
        this.editing = false;
        this.notification.showSuccess("Opis został dodany");
      },
      error: err => {
        this.notification.showError("Opis nie został dodany", err)
      }
    });
  }

  adjustHeight(element: HTMLTextAreaElement): void {
    element.style.height = 'auto';
    element.style.height = element.scrollHeight + 'px';
  }

  //SEKCJA FAVORITE
  addToFavorites(artistId: string) {
    this.favoriteService.addFavorite(artistId).subscribe(({
      next: () => {
        this.notification.showSuccess("Dodano do ulubionych");
      },
      error: (err) => {
        this.notification.showError("Nie udało się dodać do ulubionych", err);
      }
    }));
  }
  loadReviews() {
    this.reviewService.getReviewsForArtist(this.userId).subscribe({
      next: (reviews) => {
        this.reviews = reviews;
        this.calculateAverageRate();
      },
      error: (err) => {
        console.warn('Brak opinii lub brak dostępu:', err);
        this.reviews = [];
        this.averageRate = 0;
      }
    });
  }

  calculateAverageRate() {
    if (!this.reviews || this.reviews.length === 0) {
      this.averageRate = 0;
      return;
    }

    const total = this.reviews.reduce((sum, r) => sum + r.rate, 0);
    this.averageRate = total / this.reviews.length;
  }
}
