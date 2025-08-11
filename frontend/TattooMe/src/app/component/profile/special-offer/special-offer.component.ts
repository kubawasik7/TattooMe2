import { Component, Input } from '@angular/core';
import { CreateOffer, Offer, ProfileService } from '../../../service/profile.service';

@Component({
  selector: 'app-special-offer',
  standalone: false,
  templateUrl: './special-offer.component.html',
  styleUrl: './special-offer.component.css'
})
export class SpecialOfferComponent {
  @Input() userId!: string;
  @Input() isOwner = false;
  offers: Offer[] = [];
  editingId: string | null = null;
  draft: CreateOffer = { startDate: '', endDate: '', description: '' };

  constructor(private profileService: ProfileService){}

  ngOnInit(): void{
    this.load();
    
  }

  load() {
    this.profileService.getOffers().subscribe(list => this.offers = list);
  }
  startNew() {
    this.editingId = 'new';
    this.draft = { startDate: '', endDate: '', description: '' };
  }

  startEditOffer(o: Offer) {
    this.editingId = o.id;
    this.draft = {
      startDate: o.startDate,
      endDate: o.endDate,
      description: o.description
    };
  }

  save() {
    if (this.editingId === 'new') {
      this.profileService.createOffer(this.draft).subscribe(() => this.load());
    } else {
      this.profileService.updateOffer(this.editingId!, this.draft).subscribe(() => this.load());
    }
    this.editingId = null;
  }

  cancel() {
    this.editingId = null;
  }

  delete(id: string) {
    if (confirm('Usunąć tę ofertę?')) {
      this.profileService.deleteOffer(id).subscribe(() => this.load());
    }
  }

}
