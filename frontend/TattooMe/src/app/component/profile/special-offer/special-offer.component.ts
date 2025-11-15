import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { CreateOffer, Offer, ProfileService } from '../../../service/profile.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NotificationService } from '../../../service/notification.service';
import Swal from 'sweetalert2';

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
  editingForm: FormGroup | null = null;
  editingId: string | null = null;
  todayDate!: string;
  @ViewChild('autoResize') autoResizeTextarea!: ElementRef<HTMLTextAreaElement>;

  constructor(
    private profileService: ProfileService,
    private fb: FormBuilder,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.todayDate = new Date().toISOString().slice(0, 16);
    this.load();
  }

  load() {
    this.profileService.getOffers(this.userId).subscribe({
      next: (list) => (this.offers = list),
      error: (err) => {
        console.log(err)
      },
    });
  }

  startNew() {
    this.editingId = 'new';
    this.editingForm = this.fb.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      description: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(500)]]
    });
  }

  startEditOffer(offer: Offer) {
    this.editingId = offer.id;
    this.editingForm = this.fb.group({
      startDate: [offer.startDate, Validators.required],
      endDate: [offer.endDate, Validators.required],
      description: [offer.description, [Validators.required, Validators.minLength(5), Validators.maxLength(500)]]
    });

    setTimeout(() => {
      if (this.autoResizeTextarea) {
        this.adjustHeight(this.autoResizeTextarea.nativeElement);
      }
    });
  }

  cancel() {
    this.editingId = null;
    this.editingForm = null;
  }

  save() {
    if (!this.editingForm || this.editingForm.invalid) return;

    const offerData: CreateOffer = this.editingForm.value;

    if (this.editingId === 'new') {
      this.profileService.createOffer(offerData).subscribe({
        next: () => {
          this.notification.showSuccess("Promocja została dodana");
          this.load();
        },
        error: (err) => {
          this.notification.showError("Nie udało się dodać promocji");
        },
      });
    } else {
      this.profileService.updateOffer(this.editingId!, offerData).subscribe({
        next: () => {
          this.notification.showSuccess("Promocja została zaktualizowana");
          this.load();
        },
        error: (err) => {
          this.notification.showError("Nie udało się zapisać zmian");
        },
      });
    }

    this.cancel();
  }

  delete(id: string) {
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
        this.profileService.deleteOffer(id).subscribe({
          next: () => {
            this.notification.showSuccess("Promocja została usunięta");
            this.load();
          },
          error: (err) => {
            this.notification.showError("Nie udało się usunąć promocji");
          }
        });
      }
    });
  }

  adjustHeight(element: HTMLTextAreaElement) {
    element.style.height = 'auto';
    element.style.height = element.scrollHeight + 'px';
  }
}
