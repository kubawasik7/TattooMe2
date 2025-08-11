import { Component, Input } from '@angular/core';
import { Portfolio } from '../../../model/portfolio';
import { PortfolioService } from '../../../service/portfolio.service';

@Component({
  selector: 'app-portfolio',
  standalone: false,
  templateUrl: './portfolio.component.html',
  styleUrl: './portfolio.component.css'
})
export class PortfolioComponent {
  @Input() userId!: string;
  @Input() isOwner = false;
  portfolioItems: Portfolio[] = [];
  selectedFile: File | null = null;
  showAllPortfolio = false;

  constructor(private portfolioService: PortfolioService){}

  ngOnInit(): void{
      this.portfolioService.getByUser(this.userId).subscribe(items => {
      this.portfolioItems = items;
    });
  }

  deleteImagePortfolio(id: string) {
  this.portfolioService.delete(id).subscribe(() => {
    this.portfolioItems = this.portfolioItems.filter(p => p.id !== id);
  });
}

  onFileSelectedPortfolio(event: any): void {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  uploadPortfolio(): void {
    if (!this.selectedFile) return;

    this.portfolioService.uploadImage(this.selectedFile).subscribe({
      next: () => {
        this.selectedFile = null;
      },
      error: err => console.error('Błąd uploadu portfolio', err)
    });
  }
}
