import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { WorkHour } from '../../../model/work-hour';
import { WorkHourService } from '../../../service/work-hour.service';
import { NotificationService } from '../../../service/notification.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-work-hour',
  standalone: false,
  templateUrl: './work-hour.component.html',
  styleUrl: './work-hour.component.css'
})
export class WorkHourComponent implements OnInit {
  @Input() studioId!: string;
  workHours: WorkHour[] = [];
  editId: string | null = null;

  addForm: FormGroup;
  editForm: FormGroup;

  days = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

  constructor(
    private workHourService: WorkHourService,
    private fb: FormBuilder,
    private notification: NotificationService
  ) {
    this.addForm = this.fb.group({
      dayOfWeek: ['MONDAY', Validators.required],
      startTime: ['', [Validators.required]],
      endTime: ['', [Validators.required]]
    });

    this.editForm = this.fb.group({
      dayOfWeek: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.load();
  }

  load(): void {
    if (!this.studioId) return;
    this.workHourService.getWorkHours(this.studioId).subscribe(res => this.workHours = res);
  }

  add(): void {
    if (this.addForm.invalid) return;

    const p = this.addForm.value;

    if (p.endTime <= p.startTime) {
       Swal.fire({
              icon: 'warning',
              background: '#1e1e1e',
              color: '#ffffffff',
              text: 'Godzina zamknięcia nie może być wcześniejsza od godziny otwarcia',
              showConfirmButton: true
            });
      return;
    }

    this.workHourService.addWorkHour(this.studioId, p).subscribe(() => {
      this.addForm.reset({ dayOfWeek: 'MONDAY' });
      this.load();
    },
      err => this.notification.showError("Nie udalo sie dodać godzin", err));
  }

  startEdit(workHour: WorkHour): void {
    this.editId = workHour.id;
    this.editForm.setValue({
      dayOfWeek: workHour.dayOfWeek,
      startTime: workHour.startTime,
      endTime: workHour.endTime
    });
  }

  cancelEdit(): void {
    this.editId = null;
  }

  saveEdit(): void {
    if (!this.editId || this.editForm.invalid) return;

    const p = this.editForm.value;

    if (p.endTime <= p.startTime) {
      Swal.fire({
              icon: 'warning',
              background: '#1e1e1e',
              color: '#ffffffff',
              text: 'Godzina zamknięcia nie może być wcześniejsza od godziny otwarcia',
              showConfirmButton: true
            });
      return;
    }

    this.workHourService.updateWorkHour(this.editId, p).subscribe(() => {
      this.editId = null;
      this.load();
    },
      err => this.notification.showError("Nie udalo sie dodać godzin", err));
  }

  remove(workHour: WorkHour): void {
    Swal.fire({
      title: 'Usunąć tę godzinę?',
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
        this.workHourService.deleteWorkHour(this.studioId, workHour.id).subscribe({
          next: () => {
            this.load();
            Swal.fire({
              icon: 'success',
              background: '#1e1e1e',
              color: '#ffffffff',
              title: 'Usunięto!',
              text: 'Godzina została pomyślnie usunięta.',
              timer: 1500,
              showConfirmButton: false
            });
          },
          error: (err) => {
            this.notification.showError('Nie udało się usunąć godziny', err);
            Swal.fire({
              icon: 'error',
              title: 'Błąd',
              text: 'Nie udało się usunąć godziny.',
            });
          }
        });
      }
    });
  }
}