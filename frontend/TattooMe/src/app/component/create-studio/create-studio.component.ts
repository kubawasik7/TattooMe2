import { Component } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
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
      name: [''],
      city: [''],
      street: [''],
      streetNumber: [''],
      postalCode: ['']
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
