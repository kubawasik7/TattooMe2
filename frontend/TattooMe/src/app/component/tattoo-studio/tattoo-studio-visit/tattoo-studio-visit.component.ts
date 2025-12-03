import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TattooStudioVisitService } from '../../../service/tattoo-studio-visit.service';
import { NotificationService } from '../../../service/notification.service';

@Component({
  selector: 'app-tattoo-studio-visit',
  standalone: false,
  templateUrl: './tattoo-studio-visit.component.html',
  styleUrl: './tattoo-studio-visit.component.css'
})
export class TattooStudioVisitComponent {
  @Input() studioId!: string;

  visitForm!: FormGroup;
  submitting = false;

  constructor(
    private fb: FormBuilder,
    private visitService: TattooStudioVisitService,
    private notification: NotificationService
  ) {}

  ngOnInit(): void {
    this.visitForm = this.fb.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      comment: ['']
    });
  }

  submitVisit(): void {
    if (this.visitForm.invalid) return;

    this.submitting = true;

    const formValue = this.visitForm.value;

    this.visitService.createVisit(this.studioId, {
      startDate: formValue.startDate,
      endDate: formValue.endDate,
      comment: formValue.comment
    }).subscribe({
      next: () => {
        this.notification.showSuccess('Wizyta została wysłana do studia.');
        this.visitForm.reset();
        this.submitting = false;
      },
      error: err => {
        this.notification.showError('Nie udało się wysłać wizyty.');
        this.submitting = false;
      }
    });
  }
}