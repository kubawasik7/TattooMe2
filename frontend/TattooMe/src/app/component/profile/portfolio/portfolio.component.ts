import { Component, Input, OnInit } from '@angular/core';
import { Portfolio } from '../../../model/portfolio';
import { PortfolioService } from '../../../service/portfolio.service';
import { NotificationService } from '../../../service/notification.service';

@Component({
  selector: 'app-portfolio',
  standalone: false,
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.css'
})
export class PortfolioComponent implements OnInit {
  @Input() userId!: string;
  @Input() isOwner = false;

  portfolioItems: Portfolio[] = [];
  selectedFile: File | null = null;
  showAllPortfolio = false;

  constructor(private portfolioService: PortfolioService, private notification: NotificationService) {}

  ngOnInit(): void {
    this.loadPortfolio();
  }

  private loadPortfolio(): void {
    this.portfolioService.getByUser(this.userId).subscribe({
      next: items => (this.portfolioItems = items),
      error: err => this.notification.showError("Nie udało się pobrać portfolio")
    });
  }

  deleteImagePortfolio(id: string): void {
    this.portfolioService.delete(id).subscribe({
      next: () => {
        this.portfolioItems = this.portfolioItems.filter(p => p.id !== id);
        this.notification.showSuccess("Zdjęcie zostało usunięte");
      },
      error: err => this.notification.showError("Nie udalo sie usunac zdjecia", err)
    });
  }

  onFileSelectedPortfolio(event: any): void {
    const file = event.target.files?.[0];
    if (file) {
      this.selectedFile = file;
    }
  }

  clearSelectedFile(): void {
    this.selectedFile = null;
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) fileInput.value = '';
  }

  uploadPortfolio(): void {
    if (!this.selectedFile) return;

    this.portfolioService.uploadImage(this.selectedFile).subscribe({
      next: () => {
        this.selectedFile = null;
        this.notification.showSuccess("Zdjęcie zostało dodane");
        this.loadPortfolio();
      },
      error: err => this.notification.showError("Nie udalo sie dodac zdjęcia")
    });
  }

  toggleFeatured(item: Portfolio): void {
  if (!item) return;

  const newState = !item.featured;

  this.portfolioService.updateFeatured(this.userId, item.id, newState)
    .subscribe({
      next: () => {
        item.featured = newState;
      },
      error: (err) => {
        console.error('Nie udało się zmienić stanu featured:', err);
      }
    });
}
}
