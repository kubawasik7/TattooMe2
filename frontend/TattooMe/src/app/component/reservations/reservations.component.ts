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
        this.reload("Wizyta została zatwierdzona w studiu")
      );
    } else {
      this.visitService.confirmVisit(id).subscribe(() =>
        this.reload("Wizyta została potwierdzona")
      );
    }
  }

cancelVisit() {
  let visitId: string | undefined;

  if (this.selectedRoleTab === 'studio' && this.currentVisitStudio) {
    visitId = this.currentVisitStudio.id;
  } else if ((this.selectedRoleTab === 'client' || this.selectedRoleTab === 'artist') && this.currentVisitClientOrArtist) {
    visitId = this.selectedRoleTab === 'artist'
      ? this.currentVisitClientOrArtist.id
      : this.currentVisitClientOrArtist.id;
  } else {
    return;
  }

  Swal.fire({
    title: 'Usunąć tę wizytę?',
    text: 'Tej akcji nie można cofnąć.',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    background: '#1e1e1e',
    color: '#ffffffff',
    confirmButtonText: 'Tak, usuń',
    cancelButtonText: 'Anuluj'
  }).then((result) => {
    if (result.isConfirmed && visitId) {
      let cancel$: Observable<void>;
      
      if (this.selectedRoleTab === 'studio') {
        cancel$ = this.studioVisitService.cancelVisitAsStudio(visitId);
      } else {
        cancel$ = this.selectedRoleTab === 'artist'
          ? this.visitService.cancelVisitAsArtist(visitId)
          : this.visitService.cancelVisitAsClient(visitId);
      }

      cancel$.subscribe({
        next: () => {
          this.reload("Wizyta została anulowana");
          Swal.fire({
            icon: 'success',
            background: '#1e1e1e',
            color: '#ffffffff',
            title: 'Usunięto!',
            text: 'Wizyta została pomyślnie anulowana.',
            timer: 1500,
            showConfirmButton: false
          });
        },
        error: () => this.notification.showError("Nie udało się anulować wizyty")
      });
    }
  });
}


  private reload(message: string) {
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