import { Component, OnInit } from '@angular/core';
import { Visit } from '../../model/visit';
import { VISIT_STATUS, VisitService } from '../../service/visit.service';
import { forkJoin } from 'rxjs';
type TabKey = 'active' | 'past' | 'cancelled';
type VisitStatus = 'ACTIVE' | 'PAST' | 'CANCELLED';
@Component({
  selector: 'app-reservations',
  standalone: false,
  templateUrl: './reservations.component.html',
  styleUrl: './reservations.component.css'
})

export class ReservationsComponent implements OnInit {
  selectedTab: 'active' | 'past' | 'cancelled' = 'active';
  visits: Visit[] = [];

  constructor(private visitService: VisitService) {}

  ngOnInit(): void {
    this.loadVisits();
  }

  selectTab(tab: 'active' | 'past' | 'cancelled') {
    this.selectedTab = tab;
    this.loadVisits();
  }

  loadVisits() {
    if (this.selectedTab === 'active') {
      this.visitService.getActive().subscribe(visits => this.visits = visits);
    } else if (this.selectedTab === 'past') {
      this.visitService.getPast().subscribe(visits => this.visits = visits);
    } else {
      this.visitService.getCancelled().subscribe(visits => this.visits = visits);
    }

  }
}