import { Component, OnInit } from '@angular/core';
import { TattooStudio } from '../../model/tattoo-studio';
import { ActivatedRoute } from '@angular/router';
import { StudioService } from '../../service/studio.service';

@Component({
  selector: 'app-tattoo-studio',
  standalone: false,
  templateUrl: './tattoo-studio.component.html',
  styleUrl: './tattoo-studio.component.css'
})
export class TattooStudioComponent implements OnInit{
  tattooStudio!: TattooStudio;
  studioId!: string;

    constructor(
    private route: ActivatedRoute,
    private studioService: StudioService

  ) { }


  ngOnInit(): void {
  this.studioId = this.route.snapshot.paramMap.get('id')!;
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
