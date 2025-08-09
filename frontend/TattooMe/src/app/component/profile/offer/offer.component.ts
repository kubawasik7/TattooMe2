import { Component, Input } from '@angular/core';
import { FlashService } from '../../../service/flash.service';
import { Flash } from '../../../model/flash';

@Component({
  selector: 'app-offer',
  standalone: false,
  templateUrl: './offer.component.html',
  styleUrl: './offer.component.css'
})
export class OfferComponent {
  @Input() userId!: string;
  @Input() isOwner = false;
  flashes: Flash[] = [];
  showFlashModal = false;
  flashFile: File | null = null;
  newFlash: Flash = {
    description: '',
    reccomendedPlace: '',
    sizeMin: 0,
    sizeMax: 0,
    priceMin: 0,
    priceMax: 0
  };

  constructor(private flashService: FlashService) { }

  ngOnInit(): void {
    this.loadFlashes();
  }

  openFlashModal(): void {
    this.showFlashModal = true;
  }

  closeFlashModal(): void {
    this.showFlashModal = false;
    this.flashFile = null;
    this.newFlash = {
      description: '',
      reccomendedPlace: '',
      sizeMin: 0, sizeMax: 0, priceMin: 0, priceMax: 0
    };
  }

  onFlashFileSelected(evt: Event): void {
    const input = evt.target as HTMLInputElement;
    if (input.files?.length) {
      this.flashFile = input.files[0];
    }
  }

  loadFlashes(): void {
    this.flashService.getByUser(this.userId).subscribe({
      next: (data) => this.flashes = data,
      error: (e) => console.error('Błąd pobierania flashy', e)
    });
  }

  submitFlash(): void {
    if (!this.flashFile) return;

    const form = new FormData();
    form.append('file', this.flashFile);
    form.append(
      'data',
      new Blob([JSON.stringify(this.newFlash)], { type: 'application/json' })
    );

    this.flashService.upload(form).subscribe({
      next: () => { this.closeFlashModal(); this.loadFlashes(); },
      error: (e) => console.error('Błąd uploadu flasha', e)
    });
  }
}
