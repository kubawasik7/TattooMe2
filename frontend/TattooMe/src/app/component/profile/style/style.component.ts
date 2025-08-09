import { Component, Input, OnInit } from '@angular/core';
import { TattooStyle } from '../../../model/tattoo-style';
import { WorkStyleService } from '../../../service/work-style.service';

@Component({
  selector: 'app-style',
  standalone: false,
  templateUrl: './style.component.html',
  styleUrl: './style.component.css'
})
export class StyleComponent implements OnInit {
  @Input() userId!: string;
  @Input() isOwner = false;
  editing = false;
  styles: TattooStyle[] = [];
  allStyles: TattooStyle[] = [];
  editStyleMode = false;
  selectedStyleIds: string[] = [];
  originalSelectedStyleIds: string[] = [];

  constructor(private workStyleService: WorkStyleService) { }

  ngOnInit(): void {
    this.workStyleService.getAllStyles().subscribe(styles => this.allStyles = styles);

    this.workStyleService.getUserStyles(this.userId).subscribe(styles => {
      this.styles = styles;
      this.selectedStyleIds = styles.map(s => s.id);
    });

  }
  startEditing(): void {
    this.editStyleMode = true;
    this.originalSelectedStyleIds = [...this.selectedStyleIds]; 
  }

  cancelEditing(): void {
    this.selectedStyleIds = [...this.originalSelectedStyleIds];
    this.editStyleMode = false;
  }

  saveStyles(): void {
    if (!this.userId) {
      console.error('Brak userId');
      return;
    }

    this.workStyleService.saveUserStyles(this.userId, this.selectedStyleIds)
      .subscribe(() => {
        this.editStyleMode = false;
        alert('Zapisano style.');
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
