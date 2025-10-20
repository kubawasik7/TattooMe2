import { Component, OnInit } from '@angular/core';
import { Visit } from '../../model/visit';
import { VISIT_STATUS, VisitService } from '../../service/visit.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-reservations',
  standalone: false,
  templateUrl: './reservations.component.html',
  styleUrl: './reservations.component.css'
})

export class ReservationsComponent implements OnInit {
  selectedRoleTab: 'client' | 'artist' = 'client';
  selectedTab: 'active' | 'past' | 'cancelled' = 'active';
  visits: Visit[] = [];
  currentVisit?: Visit;
  showDetails: boolean = false;

  constructor(private visitService: VisitService, public authService: AuthService) { }

  ngOnInit(): void {
    this.loadVisits();
  }

  selectTab(tab: 'active' | 'past' | 'cancelled') {
    this.selectedTab = tab;
    this.loadVisits();
  }
  loadVisits() {
    if (this.selectedRoleTab === 'client') {
      if (this.selectedTab === 'active') this.visitService.getActive().subscribe(v => this.visits = v);
      else if (this.selectedTab === 'past') this.visitService.getPast().subscribe(v => this.visits = v);
      else this.visitService.getCancelled().subscribe(v => this.visits = v);
    } else {
      if (this.selectedTab === 'active') this.visitService.getActiveAsArtist().subscribe(v => this.visits = v);
      else if (this.selectedTab === 'past') this.visitService.getPastAsArtist().subscribe(v => this.visits = v);
      else this.visitService.getCancelledAsArtist().subscribe(v => this.visits = v);
    }
  }

  confirmVisit(id: string) {
    this.visitService.confirmVisit(id).subscribe(() => {
      this.loadVisits();
      this.closeDetails();
    });
  }

 cancelVisit() {
    if (!this.currentVisit) return;

    const isArtist = this.selectedRoleTab === 'artist';
    const cancel$ = isArtist 
      ? this.visitService.cancelVisitAsArtist(this.currentVisit.id)
      : this.visitService.cancelVisitAsClient(this.currentVisit.id);

    cancel$.subscribe(() => {
      this.loadVisits();
      this.closeDetails();
    });
  }

  openVisitDetails(visitId: string) {
    this.visitService.getById(visitId).subscribe(details => {
      this.currentVisit = details;
      this.showDetails = true;
    });
  }

  closeDetails() {
    this.showDetails = false;
    this.currentVisit = undefined;
  }
}