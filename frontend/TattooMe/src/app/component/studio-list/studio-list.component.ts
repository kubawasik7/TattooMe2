import { Component } from '@angular/core';
import { TattooStudio } from '../../model/tattoo-studio';
import { StudioService } from '../../service/studio.service';

@Component({
  selector: 'app-studio-list',
  standalone: false,
  templateUrl: './studio-list.component.html',
  styleUrl: './studio-list.component.css'
})
export class StudioListComponent {
  studios: TattooStudio[] = [];
  p = 1;
  constructor(private studioService: StudioService) { }

  ngOnInit(): void {
    this.studioService.getStudios().subscribe({
      next: data => { this.studios = data }
    })
  }
}
