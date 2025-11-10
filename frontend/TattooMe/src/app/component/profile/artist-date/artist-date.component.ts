import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators } from '@angular/forms';
import { CreateSlot } from '../../../model/create-slot';
import { ScheduleSlot } from '../../../model/schedule-slot';
import { ArtistDateService } from '../../../service/artist-date.service';
import { NotificationService } from '../../../service/notification.service';

@Component({
  selector: 'app-artist-date',
  standalone: false,
  templateUrl: './artist-date.component.html',
  styleUrl: './artist-date.component.css'
})
export class ArtistDateComponent implements OnInit {
  @Input() userId!: string;
  @Input() isOwner = false;
  @Input() isLoggedIn = false;
  @Input() studioId?: string; 

  slots: ScheduleSlot[] = [];
  isNewOpen = false;
  isBookingOpen = false;
  showVisitModal = false;
  showAll = false;

  currentSlot?: ScheduleSlot;
  selectedSlotId = '';

  slotForm!: FormGroup<{ dateTime: FormControl<string> }>;
  bookingForm!: FormGroup<{ clientName: FormControl<string>; contact: FormControl<string> }>;

  constructor(
    private artistDateService: ArtistDateService,
    private fb: FormBuilder,
    private notification: NotificationService
  ) {}

  ngOnInit(): void {
    this.initForms();
    this.loadSlots();
  }

  private initForms(): void {
    this.slotForm = this.fb.nonNullable.group({
      dateTime: ['', Validators.required],
    });

    this.bookingForm = this.fb.nonNullable.group({
      clientName: ['', Validators.required],
      contact: ['', Validators.required],
    });
  }

  private loadSlots(): void {
    if (this.isOwner) {
      this.artistDateService.getSlots().subscribe({
        next: (s) => (this.slots = s),
        error: (err) => this.notification.showError("Nie udało się załadować terminów", err)
      });
    } else {
      this.artistDateService.getAvailableDates(this.userId).subscribe({
        next: (s) => (this.slots = s),
        error: (err) => this.notification.showError("Nie udało się załadować terminów", err)
      });
    }
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

    const dto: CreateSlot = this.slotForm.getRawValue();
    this.artistDateService.createSlot(dto).subscribe({
      next: () => {
        this.closeNew();
        this.notification.showSuccess("Termin został utworzony");
        this.loadSlots();
      },
      error: (err) => this.notification.showError("Nie udało się dodać terminu", err)
    });
  }

  delete(id: string): void {
    if (!confirm('Usunąć ten termin?')) return;

    this.artistDateService.deleteSlot(id).subscribe({
      next: () =>{ 
        this.notification.showSuccess("Termin został usunięty");
        this.loadSlots()
      },
      error: (err) => this.notification.showError("Nie udało się usunąć terminu", err)
    });
  }

  toggle(id: string): void {
    this.artistDateService.toggleSlot(id).subscribe({
      next: () => this.loadSlots(),
      error: (err) => this.notification.showError("Nie udało się zmienić dostępnosci terminu", err)
    });
  }

  book(slot: ScheduleSlot): void {
    this.currentSlot = slot;
    this.selectedSlotId = slot.id;
    this.isBookingOpen = true;
    this.showVisitModal = true;
    this.bookingForm.reset();
  }
}