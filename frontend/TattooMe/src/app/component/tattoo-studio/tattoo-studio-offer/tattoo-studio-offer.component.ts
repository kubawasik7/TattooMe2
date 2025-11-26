import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { FlashService } from '../../../service/flash.service';
import { NotificationService } from '../../../service/notification.service';
import { TattooStudioOffer } from '../../../model/tattoo-studio-offer';
import { Flash } from '../../../model/flash';
import { TattooStudioOfferService } from '../../../service/tattoo-studio-offer.service';
import { error } from 'console';
import { FlashOffer } from '../../../model/flash-offer';
import { FlashOfferService } from '../../../service/flash-offer.service';

@Component({
  selector: 'app-tattoo-studio-offer',
  standalone: false,
  templateUrl: './tattoo-studio-offer.component.html',
  styleUrl: './tattoo-studio-offer.component.css'
})
export class TattooStudioOfferComponent implements OnInit {
 
  @Input() studioId!: string;
  @Input() currentUserRole: string | null = null;
    @Input() isOwner = false; 

  offers: TattooStudioOffer[] = [];
  flashes: Flash[] = [];
  flashOffersMap: Record<string, FlashOffer | null> = {};
  todayDate!: string;

  constructor(
    private studioOfferService: TattooStudioOfferService,
    private flashService: FlashService,
    private flashOfferService: FlashOfferService
  ) {}

  ngOnInit(): void {
    this.todayDate = new Date().toISOString().slice(0, 16);
    this.loadOffersAndFlashes();
  }

  loadOffersAndFlashes(): void {
    this.studioOfferService.getCombinedOffers(this.studioId).subscribe({
      next: offers => {
        this.offers = offers;

        this.offers.forEach(o => {
          this.flashOfferService.getByArtistOffer(o.id).subscribe(flashOffers => {
            this.flashOffersMap[o.id] = flashOffers.length ? flashOffers[0] : null;
          });
        });
      },
      error: err => console.error(err)
    });

    this.flashService.getFlashesFromStudio(this.studioId).subscribe({
      next: list => this.flashes = list,
      error: err => console.error(err)
    });
  }
}
