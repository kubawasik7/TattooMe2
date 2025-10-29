import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { WorkHour } from '../../../model/work-hour';
import { WorkHourService } from '../../../service/work-hour.service';

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

  constructor(private whService: WorkHourService, private fb: FormBuilder) {
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
    this.whService.getWorkHours(this.studioId).subscribe(res => this.workHours = res);
  }

  add(): void {
    if (this.addForm.invalid) return;
    const p = this.addForm.value;
    if (p.endTime <= p.startTime) { alert('End time must be after start time'); return; }
    this.whService.addWorkHour(this.studioId, p).subscribe(() => {
      this.addForm.reset({ dayOfWeek: 'MONDAY' });
      this.load();
    }, err => alert('Błąd: ' + err.error?.message || err.statusText));
  }

  startEdit(wh: WorkHour): void {
    this.editId = wh.id;
    this.editForm.setValue({
      dayOfWeek: wh.dayOfWeek,
      startTime: wh.startTime,
      endTime: wh.endTime
    });
  }

  cancelEdit(): void {
    this.editId = null;
  }

  saveEdit(): void {
    if (!this.editId || this.editForm.invalid) return;
    const p = this.editForm.value;
    if (p.endTime <= p.startTime) { alert('End time must be after start time'); return; }
    this.whService.updateWorkHour(this.editId, p).subscribe(() => {
      this.editId = null;
      this.load();
    }, err => alert('Błąd: ' + (err.error?.message || err.statusText)));
  }

  remove(wh: WorkHour): void {
    if (!confirm('Usunąć tę godzinę?')) return;
    this.whService.deleteWorkHour(this.studioId, wh.id).subscribe(() => this.load(),
      err => alert('Błąd: ' + (err.error?.message || err.statusText)));
  }
}