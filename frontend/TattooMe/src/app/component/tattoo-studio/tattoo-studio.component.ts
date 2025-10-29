import { Component, OnInit } from '@angular/core';
import { TattooStudio } from '../../model/tattoo-studio';
import { ActivatedRoute } from '@angular/router';
import { StudioService } from '../../service/studio.service';
import { AuthService } from '../../service/auth.service';
import { NotificationService } from '../../service/notification.service';

@Component({
  selector: 'app-tattoo-studio',
  standalone: false,
  templateUrl: './tattoo-studio.component.html',
  styleUrl: './tattoo-studio.component.css'
})
export class TattooStudioComponent implements OnInit {
  tattooStudio!: TattooStudio;
  studioId!: string;
  showWorkHourEditor = false;
  defaultAvatar = '/pobrane.png';

  constructor(
    private route: ActivatedRoute,
    private studioService: StudioService,
    private authService: AuthService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.studioId = this.route.snapshot.paramMap.get('id')!;

    this.studioService.getStudioById(this.studioId).subscribe({
      next: studio => {
        this.tattooStudio = studio;
      },
      error: err => {
        this.notification.showError("Nie udało się załadować studio", err);
      }
    });
  }
}
