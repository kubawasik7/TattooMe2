import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TattooStudioVisitService } from '../../../service/tattoo-studio-visit.service';
import { NotificationService } from '../../../service/notification.service';
import { StudioService } from '../../../service/studio.service';
import { StudioArtist } from '../../../model/studio-artist';

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
  showForm = false;
  isMember: boolean = false;
  isOwner: boolean = false;
  currentUserRole: string | null = null;
  studioArtists: StudioArtist[] = [];
  currentUserId: string | null = null;

  constructor(
    private fb: FormBuilder,
    private visitService: TattooStudioVisitService,
    private notification: NotificationService,
    private studioService: StudioService
  ) { }

  ngOnInit(): void {
    this.visitForm = this.fb.group({
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      comment: ['']
    });
  }

  toggleForm(): void {
    this.showForm = !this.showForm;
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
        this.showForm = false;
      },
      error: () => {
        this.notification.showError('Nie udało się wysłać wizyty.');
        this.submitting = false;
      }
    });
  }
  loadMembers(): void {
    this.studioService.getUsersByStudioId(this.studioId).subscribe({
      next: data => {
        this.studioArtists = data;
        const current = this.studioArtists.find(a => a.id === this.currentUserId);
        this.currentUserRole = current?.studioRole || null;
        this.isMember = !!current;
        this.isOwner = this.currentUserRole === 'OWNER';
      },
      error: () => {
        console.log("Nie udało się załadować członków");
      }
    });
  }
}