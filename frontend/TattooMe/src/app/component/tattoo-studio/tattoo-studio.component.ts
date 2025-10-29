import { Component, OnInit } from '@angular/core';
import { TattooStudio } from '../../model/tattoo-studio';
import { ActivatedRoute } from '@angular/router';
import { StudioService } from '../../service/studio.service';
import { AuthService } from '../../service/auth.service';

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
  authUserId: string | null = null;
  constructor(
    private route: ActivatedRoute,
    private studioService: StudioService,
    private authService: AuthService
  ) { }


  ngOnInit(): void {
    this.studioId = this.route.snapshot.paramMap.get('id')!;
    this.authUserId = this.authService.getUserId();
    console.log('Studio ID z URL:', this.studioId);

    this.studioService.getStudioById(this.studioId).subscribe({
      next: studio => {
        console.log('Odpowiedź z backendu:', studio);
        this.tattooStudio = studio;
      },
      error: err => {
        console.error('Błąd pobierania studia:', err);
      }
    });
  }

}
