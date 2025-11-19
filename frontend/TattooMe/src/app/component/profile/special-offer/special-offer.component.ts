import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { CreateOffer, Offer, ProfileService } from '../../../service/profile.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NotificationService } from '../../../service/notification.service';
import Swal from 'sweetalert2';
import { Flash } from '../../../model/flash';
import { FlashOfferService } from '../../../service/flash-offer.service';
import { FlashService } from '../../../service/flash.service';
import { FlashOffer } from '../../../model/flash-offer';

@Component({
  selector: 'app-special-offer',
  standalone: false,
  templateUrl: './special-offer.component.html',
  styleUrl: './special-offer.component.css'
})
export class SpecialOfferComponent {
  @Input() userId!: string;
  @Input() isOwner = false;

  offers: Offer[] = [];
  flashes: Flash[] = [];
  flashOffersMap: Record<string, FlashOffer | null> = {};
  editingForm: FormGroup | null = null;
  editingId: string | null = null;
  todayDate!: string;
  flashListOpen = false;
  selectedFlash: any = null;
  @ViewChild('autoResize') autoResizeTextarea!: ElementRef<HTMLTextAreaElement>;

  constructor(
    private profileService: ProfileService,
    private fb: FormBuilder,
    private notification: NotificationService,
    private flashOfferService: FlashOfferService,
    private flashService: FlashService
  ) { }

  ngOnInit(): void {
    this.todayDate = new Date().toISOString().slice(0, 16);
    this.loadOffers();
    this.loadFlashes();
  }

  loadOffers() {
    this.profileService.getOffers(this.userId).subscribe({
      next: (list) => {
        this.offers = list;

        this.offers.forEach(o => {
          this.flashOfferService.getByArtistOffer(o.id).subscribe(flashOffers => {
            this.flashOffersMap[o.id] = flashOffers.length ? flashOffers[0] : null;
          });
        });
      },
      error: (err) => console.error(err),
    });
  }

  loadFlashes() {
    this.flashService.getByUser(this.userId).subscribe({
      next: list => this.flashes = list,
      error: err => console.error(err)
    });
  }

  startNew() {
    this.editingId = 'new';
    this.editingForm = this.fb.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      description: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(500)]],
      flashId: [null],
      percentOff: [0, [Validators.required, Validators.min(0), Validators.max(100)]]
    });
  }

  startEditOffer(offer: Offer) {
    this.editingId = offer.id;
    const assignedFlashOffer = this.flashOffersMap[offer.id];

    this.editingForm = this.fb.group({
      startDate: [offer.startDate, Validators.required],
      endDate: [offer.endDate, Validators.required],
      description: [offer.description, [Validators.required, Validators.minLength(5), Validators.maxLength(500)]],
      flashId: [assignedFlashOffer?.flashId ?? null],
      percentOff: [assignedFlashOffer?.percentOff ?? 0, [Validators.required, Validators.min(0), Validators.max(100)]]
    });

    setTimeout(() => {
      if (this.autoResizeTextarea) this.adjustHeight(this.autoResizeTextarea.nativeElement);
    });
  }

  save() {
    if (!this.editingForm || this.editingForm.invalid) return;
    const data: CreateOffer & { flashId?: string; percentOff?: number } = this.editingForm.value;

    if (this.editingId === 'new') {
      this.profileService.createOffer(data).subscribe({
        next: (createdOffer) => {
          if (data.flashId && data.percentOff != null) {
            this.flashOfferService.create({
              tattooArtistOfferId: createdOffer.id,
              flashId: data.flashId,
              percentOff: data.percentOff
            }).subscribe({
              next: () => this.notification.showSuccess("Promocja i flash zostały dodane"),
              error: () => this.notification.showError("Nie udało się dodać flasha do promocji")
            });
          } else {
            this.notification.showSuccess("Promocja została dodana");
          }
          this.loadOffers();
        },
        error: () => this.notification.showError("Nie udało się dodać promocji")
      });
    } else {
      this.profileService.updateOffer(this.editingId!, data).subscribe({
        next: () => {
          if (data.flashId && data.percentOff != null) {
            this.flashOfferService.create({
              tattooArtistOfferId: this.editingId!,
              flashId: data.flashId,
              percentOff: data.percentOff
            }).subscribe({
              next: () => this.notification.showSuccess("Promocja i flash zostały zaktualizowane"),
              error: () => this.notification.showError("Nie udało się dodać/zmienić flasha")
            });
          } else {
            this.notification.showSuccess("Promocja została zaktualizowana");
          }
          this.loadOffers();
        },
        error: () => this.notification.showError("Nie udało się zapisać zmian")
      });
    }

    this.cancel();
  }

  delete(id: string) {
    const flashOfferId = this.flashOffersMap[id]?.id;
    if (flashOfferId) this.flashOfferService.delete(flashOfferId).subscribe();

    Swal.fire({
      title: 'Usunąć tę promocję?',
      text: 'Tej akcji nie można cofnąć.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      background: '#1e1e1e',
      color: '#ffffff',
      confirmButtonText: 'Tak, usuń',
      cancelButtonText: 'Anuluj'
    }).then((result) => {
      if (result.isConfirmed) {
        this.profileService.deleteOffer(id).subscribe(() => {
          this.notification.showSuccess("Promocja została usunięta");
          this.loadOffers();
        });
      }
    });
  }

  cancel() {
    this.editingId = null;
    this.editingForm = null;
  }

  toggleFlashList() {
    this.flashListOpen = !this.flashListOpen;
  }

  selectFlash(flash: any) {
    this.selectedFlash = flash;
    this.editingForm?.get('flashId')?.setValue(flash.id);
    this.flashListOpen = false;
  }
  clearFlash() {
    this.selectedFlash = null;
    if (this.editingForm) {
      this.editingForm.get('flashId')?.setValue(null);
    }
  }

  adjustHeight(element: HTMLTextAreaElement) {
    element.style.height = 'auto';
    element.style.height = element.scrollHeight + 'px';
  }
}
