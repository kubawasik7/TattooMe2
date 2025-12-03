import { Component, OnInit } from '@angular/core';
import { Visit } from '../../model/visit';
import { VISIT_STATUS, VisitService } from '../../service/visit.service';
import { AuthService } from '../../service/auth.service';
import { NotificationService } from '../../service/notification.service';
import Swal from 'sweetalert2';
import { TattooStudioVisitService } from '../../service/tattoo-studio-visit.service';
import { StudioVisit } from '../../model/studio-visit';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-reservations',
  standalone: false,
  templateUrl: './reservations.component.html',
  styleUrl: './reservations.component.css'
})

export class ReservationsComponent implements OnInit {
  selectedRoleTab: 'client' | 'artist' | 'studio' = 'client';
  selectedTab: 'active' | 'past' | 'cancelled' = 'active';

  visitsClientOrArtist: Visit[] = [];
  visitsStudio: StudioVisit[] = [];

  currentVisitClientOrArtist?: Visit;
  currentVisitStudio?: StudioVisit;

  showDetails: boolean = false;

  constructor(
    private visitService: VisitService,
    private studioVisitService: TattooStudioVisitService,
    public authService: AuthService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.loadVisits();
  }

  loadVisits(): void {
    if (this.selectedRoleTab === 'client') {
      const methods = {
        active: this.visitService.getActive.bind(this.visitService),
        past: this.visitService.getPast.bind(this.visitService),
        cancelled: this.visitService.getCancelled.bind(this.visitService),
      };
      methods[this.selectedTab]().subscribe({
        next: (visits) => (this.visitsClientOrArtist = visits),
        error: (err) => console.error(err),
      });
    } else if (this.selectedRoleTab === 'artist') {
      const methods = {
        active: this.visitService.getActiveAsArtist.bind(this.visitService),
        past: this.visitService.getPastAsArtist.bind(this.visitService),
        cancelled: this.visitService.getCancelledAsArtist.bind(this.visitService),
      };
      methods[this.selectedTab]().subscribe({
        next: (visits) => (this.visitsClientOrArtist = visits),
        error: (err) => console.error(err),
      });
    } else if (this.selectedRoleTab === 'studio') {
      const methods = {
        active: this.studioVisitService.getActive.bind(this.studioVisitService),
        past: this.studioVisitService.getPast.bind(this.studioVisitService),
        cancelled: this.studioVisitService.getCancelled.bind(this.studioVisitService),
      };
      methods[this.selectedTab]().subscribe({
        next: (visits) => (this.visitsStudio = visits),
        error: (err) => console.error(err),
      });
    }
  }

  confirmVisit(id: string) {
    if (this.selectedRoleTab === 'studio') {
      this.studioVisitService.confirmVisit(id).subscribe(() =>
        this.reloadAfterAction("Wizyta została zatwierdzona w studiu")
      );
    } else {
      this.visitService.confirmVisit(id).subscribe(() =>
        this.reloadAfterAction("Wizyta została potwierdzona")
      );
    }
  }

  cancelVisit() {
    let cancel$: Observable<void>;

    if (this.selectedRoleTab === 'studio' && this.currentVisitStudio) {
      cancel$ = this.studioVisitService.cancelVisitAsStudio(this.currentVisitStudio.id);
    } else if ((this.selectedRoleTab === 'client' || this.selectedRoleTab === 'artist') && this.currentVisitClientOrArtist) {
      cancel$ = this.selectedRoleTab === 'artist'
        ? this.visitService.cancelVisitAsArtist(this.currentVisitClientOrArtist.id)
        : this.visitService.cancelVisitAsClient(this.currentVisitClientOrArtist.id);
    } else {
      return;
    }

    cancel$.subscribe({
      next: () => this.reloadAfterAction("Wizyta została anulowana"),
      error: () => this.notification.showError("Nie udało się anulować wizyty")
    });
  }

  private reloadAfterAction(message: string) {
    this.loadVisits();
    this.closeDetails();
    this.notification.showSuccess(message);
  }

  selectTab(tab: 'active' | 'past' | 'cancelled') {
    this.selectedTab = tab;
    this.loadVisits();
  }

  openVisitDetails(visitId: string) {
    if (this.selectedRoleTab === 'studio') {
      this.studioVisitService.getById(visitId).subscribe(details => {
        this.currentVisitStudio = details;
        this.showDetails = true;
      });
    } else {
      this.visitService.getById(visitId).subscribe(details => {
        this.currentVisitClientOrArtist = details;
        this.showDetails = true;
      });
    }
  }

  isVisit(v: Visit | StudioVisit | undefined): v is Visit {
    return v != null && 'clientName' in v;
  }

  closeDetails() {
    this.showDetails = false;
    this.currentVisitClientOrArtist = undefined;
    this.currentVisitStudio = undefined;
  }
}