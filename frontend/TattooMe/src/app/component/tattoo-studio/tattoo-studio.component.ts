import { Component, OnInit } from '@angular/core';
import { TattooStudio } from '../../model/tattoo-studio';
import { ActivatedRoute } from '@angular/router';
import { StudioService } from '../../service/studio.service';
import { AuthService } from '../../service/auth.service';
import { NotificationService } from '../../service/notification.service';
import { StudioArtist } from '../../model/studio-artist';

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

  constructor(
    private route: ActivatedRoute,
    private studioService: StudioService,
    private authService: AuthService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.studioId = this.route.snapshot.paramMap.get('id')!;
    this.currentUserId = this.authService.getUserId();
    this.loadStudio();
  }

  loadStudio(): void {
    this.studioService.getStudioById(this.studioId).subscribe({
      next: studio => {
        this.tattooStudio = studio;
        this.loadMembers();
      },
      error: err => {
        this.notification.showError('Nie udało się załadować studia', err);
      }
    });
  }

  loadMembers(): void {
    this.studioService.getUsersByStudioId(this.studioId).subscribe({
      next: data => {
        this.studioArtists = data;

        const current = this.studioArtists.find(a => a.id === this.currentUserId);
        this.currentUserRole = current?.studioRole || null;
      },
      error: err => {
        this.notification.showError('Nie udało się pobrać członków', err);
      }
    });
  }

  get canEdit(): boolean {
    return this.currentUserRole === 'OWNER' || this.currentUserRole === 'EDITOR';
  }
}
