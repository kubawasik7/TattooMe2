import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { StudioService } from '../../service/studio.service';
import { Router } from '@angular/router';
import { NotificationService } from '../../service/notification.service';

@Component({
  selector: 'app-create-studio',
  standalone: false,
  templateUrl: './create-studio.component.html',
  styleUrl: './create-studio.component.css'
})
export class CreateStudioComponent {
  form!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private studioService: StudioService,
    private router: Router,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]],
      city: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(40)]],
      street: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(35)]],
      streetNumber: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(10)]],
      postalCode: ['', [Validators.required, Validators.pattern(/^\d{2}-\d{3}$/)]]
    });
  }

  submit(): void {
    if (this.form.invalid) return;
    this.studioService.createStudio(this.form.value).subscribe({
      next: (studioId) => {
        this.router.navigate(['/studio', studioId])
        this.notification.showSuccess("Studio zostało utworzone");
      },
      error: (err) => this.notification.showError("Błąd tworzenia studia", err)
    });
  }
}
