import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ArtistDateService, CreateSlot, ScheduleSlot } from '../../service/artist-date.service';

@Component({
  selector: 'app-artist-date',
  standalone: false,
  templateUrl: './artist-date.component.html',
  styleUrls: ['./artist-date.component.css'],
})
export class ArtistDateComponent implements OnInit {
  slots: ScheduleSlot[] = [];

  isNewOpen = false;
  isBookingOpen = false;
  currentSlot?: ScheduleSlot;

slotForm!: FormGroup<{ dateTime: FormControl<string> }>;
  bookingForm!: FormGroup<{ clientName: FormControl<string>; contact: FormControl<string> }>;

  constructor(
    private artistDateService: ArtistDateService,
    private fb: FormBuilder
  ) {}
  

  ngOnInit(): void {
    this.load();
      this.slotForm = this.fb.nonNullable.group({
      dateTime: ['', Validators.required],
    });

    this.bookingForm = this.fb.nonNullable.group({
      clientName: ['', Validators.required],
      contact: ['', Validators.required],
    });
    
  }

  private load(): void {
    this.artistDateService.getSlots().subscribe(s => (this.slots = s));
  }

  openNew(): void {
    this.slotForm.reset({ dateTime: '' });
    this.isNewOpen = true;
  }
  closeNew(): void {
    this.isNewOpen = false;
  }
  saveNew(): void {
    if (this.slotForm.invalid) {
      this.slotForm.markAllAsTouched();
      return;
    }
    const dto = this.slotForm.getRawValue() as CreateSlot;

    this.artistDateService.createSlot(dto).subscribe(() => {
      this.closeNew();
      this.load();
    });
  }

  delete(id: string): void {
    if (confirm('Usunąć ten termin?')) {
      this.artistDateService.deleteSlot(id).subscribe(() => this.load());
    }
  }
  toggle(id: string): void {
    this.artistDateService.toggleSlot(id).subscribe(() => this.load());
  }

  book(slot: ScheduleSlot): void {
    this.currentSlot = slot;
    this.bookingForm.reset({ clientName: '', contact: '' });
    this.isBookingOpen = true;
  }
  closeBooking(): void {
    this.isBookingOpen = false;
    this.currentSlot = undefined;
  }
  submitBooking(): void {
    if (this.bookingForm.invalid || !this.currentSlot) {
      this.bookingForm.markAllAsTouched();
      return;
    }
    const payload = this.bookingForm.getRawValue();
 
    console.log('Zapisz rezerwację:', this.currentSlot.id, payload);
    this.closeBooking();
  }
}
