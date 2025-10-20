import { Component, Input, OnInit } from '@angular/core';
import { TattooStyle } from '../../../model/tattoo-style';
import { WorkStyleService } from '../../../service/work-style.service';
import { NotificationService } from '../../../service/notification.service';

@Component({
  selector: 'app-style',
  standalone: false,
  templateUrl: './style.component.html',
  styleUrl: './style.component.css'
})
export class StyleComponent implements OnInit {
  @Input() userId!: string;
  @Input() isOwner = false;

  styles: TattooStyle[] = [];
  allStyles: TattooStyle[] = [];
  selectedStyleIds: string[] = []; //aktualnie wybrane style przez uzytkownika
  originalSelectedStyleIds: string[] = []; //kopia stylów z przed edycji
  editing = false;

  constructor(private workStyleService: WorkStyleService, private notification: NotificationService) {}

  ngOnInit(): void {
    this.loadUserStyles();
  }

  private loadUserStyles(): void {
    this.workStyleService.getUserStyles(this.userId).subscribe({
      next: styles => {
        this.styles = styles;
        this.selectedStyleIds = styles.map(s => s.id);
      },
      error: err => this.notification.showError("Nie udalo sie zaladowac styli", err)
    });
  }

  startEditing(): void {
    this.workStyleService.getAllStyles().subscribe({
      next: styles => this.allStyles = styles,
      error: err => this.notification.showError("Nie udalo sie zaladowac styli", err)
    });
    this.editing = true;
    this.originalSelectedStyleIds = [...this.selectedStyleIds];
  }

  cancelEditing(): void {
    this.selectedStyleIds = [...this.originalSelectedStyleIds];
    this.editing = false;
  }

  saveStyles(): void {
    if (!this.userId) return;

    this.workStyleService.saveUserStyles(this.userId, this.selectedStyleIds).subscribe({
      next: () => {
        this.editing = false;
        this.notification.showSuccess("Style zostały zapisane")
        this.loadUserStyles();
      },
      error: err => this.notification.showError("Nie udalo sie zapisac styli")
    });
  }

  toggleStyle(id: string): void {
    if (this.selectedStyleIds.includes(id)) {
      this.selectedStyleIds = this.selectedStyleIds.filter(s => s !== id);
    } else {
      this.selectedStyleIds.push(id);
    }
  }
}
