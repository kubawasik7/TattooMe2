import { Component, Input } from '@angular/core';
import { CreateOffer, Offer, ProfileService } from '../../../service/profile.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

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

  constructor(private profileService: ProfileService, private fb: FormBuilder) { }

  ngOnInit(): void {
    this.todayDate = new Date().toISOString().slice(0, 16);
    this.load();
  }

  load() {
    this.profileService.getOffers(this.userId).subscribe(list => this.offers = list);
  }

  startNew() {
    this.editingId = 'new';
    this.editingForm = this.fb.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      description: ['', [Validators.required, Validators.maxLength(500)]]
    });
  }

  startEditOffer(offer: Offer) {
    this.editingId = offer.id;
    this.editingForm = this.fb.group({
      startDate: [offer.startDate, Validators.required],
      endDate: [offer.endDate, Validators.required],
      description: [offer.description, [Validators.required, Validators.maxLength(500)]]
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
      this.profileService.createOffer(offerData).subscribe(() => this.load());
    } else {
      this.profileService.updateOffer(this.editingId!, offerData).subscribe(() => this.load());
    }

    this.cancel();
  }

  delete(id: string) {
    if (confirm('Usunąć tę ofertę?')) {
      this.profileService.deleteOffer(id).subscribe(() => this.load());
    }
  }
}
