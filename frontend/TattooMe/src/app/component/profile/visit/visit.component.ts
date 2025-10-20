import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Flash } from '../../../model/flash';
import { FlashService } from '../../../service/flash.service';
import { UserInfoService } from '../../../service/user-info.service';
import { VisitService } from '../../../service/visit.service';
import { ScheduleSlot } from '../../../model/schedule-slot';

@Component({
  selector: 'app-visit',
  standalone: false,
  templateUrl: './visit.component.html',
  styleUrl: './visit.component.css'
})
export class VisitComponent implements OnInit, OnChanges {
  @Input() artistId!: string;
  @Input() artistDateId!: string;
  @Output() close = new EventEmitter<void>();

  flashList: Flash[] = [];
  form!: FormGroup;
  showHealthForm = false;

  constructor(
    private fb: FormBuilder,
    private visitService: VisitService,
    private flashService: FlashService,
    private userInfoService: UserInfoService
  ) {}

  ngOnInit(): void {
    this.initForm();
    this.loadFlash();
    this.loadPersonInfo();
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['artistDateId'] && this.form) {
      this.form.patchValue({ artistDateId: this.artistDateId });
    }
  }

  private initForm(): void {
    this.form = this.fb.group({
      artistDateId: [this.artistDateId, Validators.required],
      flashId: [''],
      comment: [''],
      allergies: [''],
      chronicDiseases: [''],
      medicines: [''],
      experiences: ['']
    });
  }

  private loadFlash(): void {
    this.flashService.getByUser(this.artistId).subscribe({
      next: (list) => this.flashList = list,
      error: (err) => console.error('Błąd ładowania wzorów:', err)
    });
  }

  private loadPersonInfo(): void {
    this.userInfoService.getInfo().subscribe({
      next: (info) => {
        if (info) {
          this.showHealthForm = false;
          this.form.patchValue({
            allergies: info.allergies,
            chronicDiseases: info.chronicDiseases,
            medicines: info.medicines,
            experiences: info.experiences
          });
        } else {
          this.showHealthForm = true;
        }
      },
      error: (err) => {
        if (err.status === 404 || err.status === 403) {
          this.showHealthForm = true;
        } else {
          console.error('Błąd ładowania danych zdrowotnych:', err);
        }
      }
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload = {
      artistDateId: this.artistDateId,
      flashId: this.form.value.flashId || null,
      comment: this.form.value.comment || '',
      allergies: this.form.value.allergies || null,
      chronicDiseases: this.form.value.chronicDiseases || null,
      medicines: this.form.value.medicines || null,
      experiences: this.form.value.experiences || null
    };

    this.visitService.createVisit(payload).subscribe({
      next: () => {
        alert('Wizyta została zarezerwowana');
        this.form.reset();
        this.close.emit();
      },
      error: (err) => {
        console.error('Błąd rezerwacji:', err);
        alert('Nie udało się zarezerwować wizyty.');
      }
    });
  }

  cancel(): void {
    this.close.emit();
  }
}