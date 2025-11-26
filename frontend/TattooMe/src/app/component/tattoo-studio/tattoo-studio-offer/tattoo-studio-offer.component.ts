import { Component, Input, OnInit } from '@angular/core';
import { FlashService } from '../../../service/flash.service';
import { TattooStudioOffer } from '../../../model/tattoo-studio-offer';
import { Flash } from '../../../model/flash';
import { TattooStudioOfferService } from '../../../service/tattoo-studio-offer.service';
import { FlashOffer } from '../../../model/flash-offer';
import { FlashOfferService } from '../../../service/flash-offer.service';
import Swal from 'sweetalert2';
import { NotificationService } from '../../../service/notification.service';

@Component({
  selector: 'app-tattoo-studio-offer',
  standalone: false,
  templateUrl: './tattoo-studio-offer.component.html',
  styleUrl: './tattoo-studio-offer.component.css'
})
export class TattooStudioOfferComponent implements OnInit {
  @Input() studioId!: string;
  @Input() currentUserRole: string | null = null;

  offers: TattooStudioOffer[] = [];
  flashes: Flash[] = [];
  flashOffersMap: Record<string, FlashOffer | null> = {};
  todayDate!: string;

  editingOffer: TattooStudioOffer | null = null;

  constructor(
    private studioOfferService: TattooStudioOfferService,
    private flashService: FlashService,
    private flashOfferService: FlashOfferService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.todayDate = new Date().toISOString().slice(0, 16);
    this.loadOffersAndFlashes();
  }

  loadOffersAndFlashes(): void {
    this.studioOfferService.getCombinedOffers(this.studioId).subscribe({
      next: offers => {
        this.offers = offers;
        this.offers.forEach(o => {
          this.flashOfferService.getByArtistOffer(o.id).subscribe(flashOffers => {
            this.flashOffersMap[o.id] = flashOffers.length ? flashOffers[0] : null;
          });
        });
      },
      error: err => console.error(err)
    });

    this.flashService.getFlashesFromStudio(this.studioId).subscribe({
      next: list => this.flashes = list,
      error: err => console.error(err)
    });
  }

  startAddOffer(): void {
    this.editingOffer = {
      id: '',
      startDate: this.todayDate,
      endDate: this.todayDate,
      description: '',
      name: 'Studio'
    };
  }

  startEditOffer(offer: TattooStudioOffer): void {
    if (!(this.currentUserRole === 'OWNER' || this.currentUserRole === 'EDITOR')) return;
    if (offer.name !== 'Studio') return;
    this.editingOffer = { ...offer };
  }

  cancelEdit(): void {
    this.editingOffer = null;
  }

  isFormValid(): boolean {
    if (!this.editingOffer) return false;

    const start = new Date(this.editingOffer.startDate);
    const end = new Date(this.editingOffer.endDate);
    const now = new Date();

    return !!this.editingOffer.startDate &&
      !!this.editingOffer.endDate &&
      !!this.editingOffer.description &&
      this.editingOffer.description.length >= 5 &&
      this.editingOffer.description.length <= 500 &&
      start >= now &&
      end >= start;
  }

  isStartDateValid(): boolean {
    if (!this.editingOffer?.startDate) return false;
    const now = new Date();
    const start = new Date(this.editingOffer.startDate);
    return start >= now;
  }

  saveOffer(): void {
    if (!this.editingOffer || !this.isFormValid()) return;

    //aktualizacja promocji
    if (this.editingOffer.id) {
      this.studioOfferService.updateOffer(this.studioId, this.editingOffer.id, this.editingOffer)
        .subscribe({
          next: updated => {
            const idx = this.offers.findIndex(o => o.id === updated.id);
            if (idx !== -1) this.offers[idx] = updated;

            this.notification.showSuccess("Promocja została zaktualizowana");
            this.editingOffer = null;
          },
          error: err => {
            console.error(err);
            this.notification.showError("Nie udało się zaktualizować promocji");
          }
        });

      return;
    }
    //tworzenie nowej promocji
    this.studioOfferService.createOffer(this.studioId, this.editingOffer)
      .subscribe({
        next: created => {
          this.offers.push(created);
          this.editingOffer = null;
          this.notification.showSuccess("Promocja została dodana");
        },
        error: err => {
          console.error(err);
          this.notification.showError("Nie udało się dodać promocji");
        }
      });
  }

  deleteOffer(offer: TattooStudioOffer): void {
    if (!(this.currentUserRole === 'OWNER' || this.currentUserRole === 'EDITOR')) return;
    if (offer.name !== 'Studio') return;

    Swal.fire({
      title: 'Usunąć tę promocje?',
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
      if (result.isConfirmed) {
        this.studioOfferService.deleteOffer(this.studioId, offer.id).subscribe({
          next: () => {
            this.offers = this.offers.filter(o => o.id !== offer.id),
              Swal.fire({
                icon: 'success',
                background: '#1e1e1e',
                color: '#ffffffff',
                title: 'Usunięto!',
                timer: 1500,
                showConfirmButton: false
              });
          },
          error: (err) => {
            this.notification.showError('Nie udało się usunąć promocji');
          }
        });
      }
    });
  }
}

