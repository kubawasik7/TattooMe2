import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormControl, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { CreateSlot } from '../../../model/create-slot';
import { ScheduleSlot } from '../../../model/schedule-slot';
import { ArtistDateService } from '../../../service/artist-date.service';
import { NotificationService } from '../../../service/notification.service';
import Swal from 'sweetalert2';

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
  todayDate: string | undefined;
  currentSlot?: ScheduleSlot;
  selectedSlotId = '';
  slotForm!: FormGroup<{ dateTime: FormControl<string> }>;
  bookingForm!: FormGroup<{ clientName: FormControl<string>; contact: FormControl<string> }>;

  constructor(
    private artistDateService: ArtistDateService,
    private fb: FormBuilder,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.todayDate = new Date().toISOString().slice(0, 16);
    this.initForms();
    this.loadSlots();
  }

  private initForms(): void {
    this.slotForm = this.fb.nonNullable.group({
      dateTime: ['', [Validators.required, this.futureDateValidator]]
    });

    this.bookingForm = this.fb.nonNullable.group({
      clientName: ['', Validators.required],
      contact: ['', Validators.required],
    });
  }

  futureDateValidator = (control: AbstractControl) => {
    if (!control.value) return null;
    const selected = new Date(control.value);
    return selected > new Date() ? null : { notFuture: true };
  };

  get dateTime() {
    return this.slotForm?.get('dateTime') || null;
  }

  private loadSlots(): void {
    if (this.isOwner) {
      this.artistDateService.getSlots().subscribe({
        next: (s) => (this.slots = s),
        error: (err) => console.log(err)
      });
    } else {
      this.artistDateService.getAvailableDates(this.userId).subscribe({
        next: (s) => (this.slots = s),
        error: (err) => console.log(err)
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
      error: (err) => this.notification.showError("Nie udało się dodać terminu")
    });
  }

  isReserved(slot: ScheduleSlot) {
    return !!slot.reserved;
  }

  isInactive(slot: ScheduleSlot) {
    return !slot.available && !slot.reserved;
  }

  delete(id: string): void {
    Swal.fire({
      title: 'Usunąć ten termin?',
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
        this.artistDateService.deleteSlot(id).subscribe({
          next: () => {
            this.notification.showSuccess("Termin został usunięty");
            this.loadSlots();
          },
          error: (err) => {
            this.notification.showError("Nie udało się usunąć terminu");
          }
        });
      }
    });
  }

  toggle(slotId: string): void {
    const slot = this.slots.find(s => s.id === slotId);
    if (slot?.reserved) return;

    this.artistDateService.toggleSlot(slotId).subscribe({
      next: () => {
        if (slot) slot.available = !slot.available;
        this.notification.showSuccess("Status terminu został zmieniony");
      },
      error: () => this.notification.showError("Nie udało się zmienić statusu terminu")
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