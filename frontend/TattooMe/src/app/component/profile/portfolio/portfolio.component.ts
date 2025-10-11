import { Component, Input, OnInit } from '@angular/core';
import { Portfolio } from '../../../model/portfolio';
import { PortfolioService } from '../../../service/portfolio.service';

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

  constructor(private portfolioService: PortfolioService) {}

  ngOnInit(): void {
    this.loadPortfolio();
  }

  private loadPortfolio(): void {
    this.portfolioService.getByUser(this.userId).subscribe({
      next: items => (this.portfolioItems = items),
      error: err => console.error('Błąd pobierania portfolio:', err)
    });
  }

  deleteImagePortfolio(id: string): void {
    this.portfolioService.delete(id).subscribe({
      next: () => {
        this.portfolioItems = this.portfolioItems.filter(p => p.id !== id);
      },
      error: err => console.error('Błąd usuwania zdjęcia:', err)
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
        this.loadPortfolio();
      },
      error: err => console.error('Błąd uploadu portfolio:', err)
    });
  }
}
