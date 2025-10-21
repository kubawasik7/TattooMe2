import { Component, OnInit } from '@angular/core';
import { Visit } from '../../model/visit';
import { VISIT_STATUS, VisitService } from '../../service/visit.service';
import { AuthService } from '../../service/auth.service';
import { NotificationService } from '../../service/notification.service';

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

  constructor(
    private visitService: VisitService,
    public authService: AuthService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.loadVisits();
  }

  loadVisits(): void {
    const methods = {
      client: {
        active: this.visitService.getActive.bind(this.visitService),
        past: this.visitService.getPast.bind(this.visitService),
        cancelled: this.visitService.getCancelled.bind(this.visitService),
      },
      artist: {
        active: this.visitService.getActiveAsArtist.bind(this.visitService),
        past: this.visitService.getPastAsArtist.bind(this.visitService),
        cancelled: this.visitService.getCancelledAsArtist.bind(this.visitService),
      },
    };
    const method = methods[this.selectedRoleTab][this.selectedTab];

    method().subscribe({
      next: (visits) => (this.visits = visits),
      error: (err) => {
        this.notification.showError("Nie udało się załadować wizyt", err);
      },
    });

  }

  confirmVisit(id: string) {
    this.visitService.confirmVisit(id).subscribe(() => {
      this.loadVisits();
      this.closeDetails();
      this.notification.showSuccess("Wizyta została potwierdzona");
    });
  }

  cancelVisit() {
    if (!this.currentVisit) return;

    const isArtist = this.selectedRoleTab === 'artist';
    const cancel$ = isArtist
      ? this.visitService.cancelVisitAsArtist(this.currentVisit.id)
      : this.visitService.cancelVisitAsClient(this.currentVisit.id);

    cancel$.subscribe({
      next: () => {
        this.loadVisits();
        this.closeDetails();
        this.notification.showSuccess("Wizyta została anulowana");
      },
      error: (err) => {
        this.notification.showError("Nie udało się anulować wizyty", err);
      },
    });
  }

  selectTab(tab: 'active' | 'past' | 'cancelled') {
    this.selectedTab = tab;
    this.loadVisits();
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