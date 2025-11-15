import { Component, OnInit } from '@angular/core';
import { TattooStudio } from '../../model/tattoo-studio';
import { ActivatedRoute, Router } from '@angular/router';
import { StudioService } from '../../service/studio.service';
import { AuthService } from '../../service/auth.service';
import { NotificationService } from '../../service/notification.service';
import { StudioArtist } from '../../model/studio-artist';
import { ChatService } from '../../service/chat.service';

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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private chatService: ChatService,
    private studioService: StudioService,
    private authService: AuthService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.studioId = this.route.snapshot.paramMap.get('id')!;
    this.currentUserId = this.authService.getUserId();
    this.isLoggedIn = this.authService.isLoggedIn();
    this.loadStudio();
  }

  loadStudio(): void {
    this.studioService.getStudioById(this.studioId).subscribe({
      next: studio => {
        this.tattooStudio = studio;
        this.loadMembers();
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

  get canEdit(): boolean {
    return this.currentUserRole === 'OWNER' || this.currentUserRole === 'EDITOR';
  }
}
