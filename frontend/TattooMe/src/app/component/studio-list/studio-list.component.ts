import { Component, OnInit } from '@angular/core';
import { TattooStudio } from '../../model/tattoo-studio';
import { StudioService } from '../../service/studio.service';
import { FilterSortSearchService } from '../../service/filter-sort-search.service';

@Component({
  selector: 'app-studio-list',
  standalone: false,
  templateUrl: './studio-list.component.html',
  styleUrl: './studio-list.component.css'
})
export class StudioListComponent implements OnInit {
  studios: TattooStudio[] = [];
  filteredStudios: TattooStudio[] = [];
  p = 1;

  searchText = '';
  filterCity = '';
  sortOption = 'name-asc';

  constructor(
    private studioService: StudioService,
    private filterSortSearchService: FilterSortSearchService
  ) {}

  ngOnInit(): void {
    this.loadStudios();
  }

  loadStudios(): void {
    this.studioService.getStudios().subscribe({
      next: data => {
        this.studios = data.map(studio => ({
          ...studio,
          name: studio.name ?? '',
        }));
        this.applyFilters();
      },
      error: err => {
        console.error('Błąd podczas ładowania studiów:', err);
      }
    });
  }

  applyFilters(): void {
    this.filteredStudios = this.filterSortSearchService.applyFilterSort(
      this.studios,
      this.searchText,
      this.filterCity,
      this.sortOption
    );
    this.p = 1;
  }
}