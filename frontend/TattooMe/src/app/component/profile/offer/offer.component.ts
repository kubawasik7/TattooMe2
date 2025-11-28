import { Component, Input, OnInit } from '@angular/core';
import { FlashService } from '../../../service/flash.service';
import { Flash } from '../../../model/flash';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NotificationService } from '../../../service/notification.service';

@Component({
  selector: 'app-offer',
  standalone: false,
  templateUrl: './offer.component.html',
  styleUrl: './offer.component.css'
})
export class OfferComponent implements OnInit {
  @Input() userId!: string;
  @Input() isOwner = false;

  flashes: Flash[] = [];
  showFlashForm: boolean = false;
  flashFile: File | null = null;
  flashForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private flashService: FlashService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.loadFlashes();
    this.initForm();
  }

  private initForm(): void {
    this.flashForm = this.fb.group({
      description: ['', [Validators.maxLength(300)]],
      reccomendedPlace: [''],
      sizeMin: [0, [Validators.min(0)]],
      sizeMax: [0, [Validators.min(0)]],
      priceMin: [0, [Validators.min(0)]],
      priceMax: [0, [Validators.min(0)]],
    });
  }

  onFlashFileSelected(evt: Event): void {
    const input = evt.target as HTMLInputElement;
    if (input.files?.length) {
      this.flashFile = input.files[0];
    }
  }

  loadFlashes(): void {
    this.flashService.getByUser(this.userId).subscribe({
      next: (data) => (this.flashes = data),
      error: (err) => console.log(err)
    });
  }

  submitFlash(): void {
    if (!this.flashForm.valid || !this.flashFile) return;

    const form = new FormData();
    form.append('file', this.flashFile);
    form.append(
      'data',
      new Blob([JSON.stringify(this.flashForm.value)], { type: 'application/json' })
    );

    this.flashService.upload(form).subscribe({
      next: () => {
        this.closeFlashForm();
        this.notification.showSuccess("Wzór został dodany");
        this.loadFlashes();
      },
      error: (err) => this.notification.showError("Wzór nie został dodany")
    });
  }

  openFlashForm() {
    this.showFlashForm = true;
  }

  closeFlashForm() {
    this.showFlashForm = false;
  }

  clearSelectedFile(): void {
    this.flashFile = null;
  }
}