import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Flash } from '../../../model/flash';
import { ScheduleSlot, ArtistDateService } from '../../../service/artist-date.service';
import { FlashService } from '../../../service/flash.service';
import { UserInfoService } from '../../../service/user-info.service';
import { VisitService } from '../../../service/visit.service';

@Component({
  selector: 'app-visit',
  standalone: false,
  templateUrl: './visit.component.html',
  styleUrl: './visit.component.css'
})
export class VisitComponent implements OnInit {
  @Input() artistId!: string;
  @Input() artistDateId!: string;
  @Output() close = new EventEmitter<void>();
  flashList: Flash[] = [];
  availableDates: ScheduleSlot[] = [];
  form!: FormGroup;
  showHealthForm = false;

  constructor(
    private fb: FormBuilder,
    private visitService: VisitService,
    private flashService: FlashService,
    private artistDateService: ArtistDateService,
    private userInfoService: UserInfoService
  ) { }

  ngOnInit(): void {
    this.loadPersonInfo();
    this.form = this.fb.group({
      artistDateId: ['', Validators.required],
      flashId: [''],
      comment: [''],
      allergies: [''],
      chronicDiseases: [''],
      medicines: [''],
      experiences: ['']
    });

    this.flashService.getByUser(this.artistId).subscribe(list => {
      this.flashList = list;
    });

    this.artistDateService.getAvailableDates(this.artistId).subscribe(dates => {
      this.availableDates = dates;
    });
  }
  
  loadPersonInfo() {
    this.userInfoService.getInfo().subscribe(info => {
      if (info) {
        console.log("nie ma danych");
        this.showHealthForm = false;
        this.form.patchValue({ allergies: info.allergies, chronicDiseases: info.chronicDiseases, medicines: info.medicines, experiences: info.experiences });
      } else {
        this.showHealthForm = true;
      }
    });
  }

  submit(): void {
    if (this.form.invalid) return;

    const payload = {
      ...this.form.value,
      artistDateId: this.artistDateId
    };

    this.visitService.createVisit(payload).subscribe(() => {
      alert("Wizyta została zarezerwowana");
      this.form.reset();
      this.close.emit();
    });
  }

  cancel(): void {
    this.close.emit();
  }
}
