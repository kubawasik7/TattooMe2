import { Component, OnInit } from '@angular/core';
import { Visit } from '../../model/visit';
import { VisitService } from '../../service/visit.service';

@Component({
  selector: 'app-reservations',
  standalone: false,
  templateUrl: './reservations.component.html',
  styleUrl: './reservations.component.css'
})
export class ReservationsComponent implements OnInit {
  visits: Visit[] = [];
  activeVisits: Visit[] = [];
  pastVisits: Visit[] = [];
  cancelledVisits: Visit[] = [];
  selectedTab = 'active';

  constructor(private visitService: VisitService) {}

  ngOnInit(): void {
    this.visitService.getMyVisits().subscribe(data => {
      this.visits = data;
      this.activeVisits = data.filter(v => v.status === 'OCZEKUJĄCA');
      this.pastVisits = data.filter(v => v.status === 'ZAKOŃCZONA');
      this.cancelledVisits = data.filter(v => v.status === 'ANULOWANA');
    });
  }

  selectTab(tab: string): void {
    this.selectedTab = tab;
  }

  getCurrentList(): Visit[] {
    switch (this.selectedTab) {
      case 'past': return this.pastVisits;
      case 'cancelled': return this.cancelledVisits;
      default: return this.activeVisits;
    }
  }

  showDetails(visit: Visit): void {
    console.log('Szczegóły wizyty:', visit);
    // tutaj dodamy modal ze szczegółami rezerwacji
  }
}
