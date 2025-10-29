import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { WorkHour } from '../../../model/work-hour';
import { WorkHourService } from '../../../service/work-hour.service';

@Component({
  selector: 'app-work-hour-mini',
  standalone: false,
  templateUrl: './work-hour-mini.component.html',
  styleUrl: './work-hour-mini.component.css'
})
export class WorkHourMiniComponent implements OnInit{
 @Input() studioId!: string;
  @Output() openEditor = new EventEmitter<void>();
  workHours: WorkHour[] = [];

  constructor(private whService: WorkHourService) {}

  ngOnInit(): void {
    if (!this.studioId) return;
    this.whService.getWorkHours(this.studioId).subscribe(res => this.workHours = res);
  }

  toggleEditor(): void {
    this.openEditor.emit();
  }
}
