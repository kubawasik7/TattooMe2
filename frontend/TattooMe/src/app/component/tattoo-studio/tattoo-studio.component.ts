import { Component, OnInit } from '@angular/core';
import { TattooStudio } from '../../model/tattoo-studio';
import { ActivatedRoute, Router } from '@angular/router';
import { StudioService } from '../../service/studio.service';
import { AuthService } from '../../service/auth.service';
import { NotificationService } from '../../service/notification.service';
import { StudioArtist } from '../../model/studio-artist';
import { ChatService } from '../../service/chat.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-tattoo-studio',
  standalone: false,
  templateUrl: './tattoo-studio.component.html',
  styleUrl: './tattoo-studio.component.css'
})
export class TattooStudioComponent implements OnInit {
  tattooStudio!: TattooStudio;
  studioArtists: StudioArtist[] = [];
  studioId!: string;
  showWorkHourEditor = false;
  defaultAvatar = '/pobrane.png';
  currentUserRole: string | null = null;
  currentUserId: string | null = null;
  isLoggedIn = false;
  isOwner = false;
  isMember = false;
  selectedFile?: File;
  previewUrl: string | null = null;
  descriptionForm!: FormGroup;
  editing = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private chatService: ChatService,
    private studioService: StudioService,
    private authService: AuthService,
    private notification: NotificationService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    const routeId = this.route.snapshot.paramMap.get('id');

    if (routeId) {
      this.studioId = routeId;
      this.initStudio();
    } else {
      this.resolveStudioRedirect();
    }
  }

  private resolveStudioRedirect(): void {
    this.studioService.getUserStudio().subscribe({
      next: studio => {
        if (studio) {
          this.router.navigate(['/studio', studio.id]);
          return;
        } else if (this.authService.isTattooArtist()) {
          this.router.navigate(['/createStudio']);
          return;
        } else {
          this.router.navigate(['/']);
          return;
        }
      },
      error: () => {
        this.router.navigate(['/']);
      }
    });
  }

  private initStudio(): void {
    this.currentUserId = this.authService.getUserId();
    this.isLoggedIn = this.authService.isLoggedIn();

    this.descriptionForm = this.fb.group({
      description: ['', [Validators.maxLength(1000)]],
    });

    this.loadStudio();
  }


  loadStudio(): void {
    this.studioService.getStudioById(this.studioId).subscribe({
      next: studio => {
        this.tattooStudio = studio;
        this.loadMembers();
        this.previewUrl = studio.profilePicture ? `data:image/png;base64,${studio.profilePicture}` : null;
        this.descriptionForm.patchValue({ description: this.tattooStudio.description || '' });
      },
      error: err => {
        console.log("Nie udało się załadować studia");
      }
    });
  }

  loadMembers(): void {
    this.studioService.getUsersByStudioId(this.studioId).subscribe({
      next: data => {
        this.studioArtists = data;
        const current = this.studioArtists.find(a => a.id === this.currentUserId);
        this.currentUserRole = current?.studioRole || null;
        this.isMember = !!current;
        this.isOwner = this.currentUserRole === 'OWNER';
      },
      error: err => {
        console.log("Nie udało się załadować członków");
      }
    });
  }
  startChatWithStudio(): void {
    if (!this.isLoggedIn) {
      this.router.navigate(['/login']);
      return;
    }

    this.chatService.startChat(this.studioId).subscribe({
      next: chat => {
        this.router.navigate(['/chat'], { queryParams: { chatId: chat.id } });
      },
      error: err => {
        console.log("Nie udało się rozpocząc rozmowy");
      }
    });
  }

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;
    this.selectedFile = input.files[0];

    const reader = new FileReader();
    reader.onload = () => this.previewUrl = reader.result as string;
    reader.readAsDataURL(this.selectedFile);
  }

  uploadAvatar(): void {
    if (!this.selectedFile) return;

    this.studioService.uploadAvatar(this.tattooStudio.id, this.selectedFile)
      .subscribe({
        next: updatedStudio => {
          this.tattooStudio = updatedStudio;
          this.previewUrl = updatedStudio.profilePicture
            ? `data:image/png;base64,${updatedStudio.profilePicture}`
            : null;
          this.notification.showSuccess("Zdjęcie zostało dodane");
          this.selectedFile = undefined;
        },
        error: () => this.notification.showError("Nie udało się dodać zdjęcia")
      });
  }

  get canEdit(): boolean {
    return this.currentUserRole === 'OWNER' || this.currentUserRole === 'EDITOR';
  }

  startEdit(): void {
    this.editing = true;
    setTimeout(() => {
      const textarea = document.querySelector<HTMLTextAreaElement>('textarea');
      if (textarea) this.adjustHeight(textarea);
    });
  }

  cancelEdit(): void {
    this.editing = false;
    this.descriptionForm.patchValue({ description: this.tattooStudio.description });
  }

  get descriptionControl() {
    return this.descriptionForm.get('description');
  }

  saveDescription(): void {
    if (this.descriptionForm.invalid) return;

    const newDescription = this.descriptionForm.value.description;

    this.studioService.updateDescription(this.studioId, newDescription).subscribe({
      next: updated => {
        this.tattooStudio.description = updated.description;
        this.editing = false;
        this.notification.showSuccess("Opis został dodany");
      },
      error: err => {
        this.notification.showError("Opis nie został dodany")
      }
    });
  }

  adjustHeight(element: HTMLTextAreaElement): void {
    element.style.height = 'auto';
    element.style.height = element.scrollHeight + 'px';
  }


}
