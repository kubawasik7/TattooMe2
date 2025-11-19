import { TestBed } from '@angular/core/testing';

import { FlashOfferService } from './flash-offer.service';

describe('FlashOfferService', () => {
  let service: FlashOfferService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FlashOfferService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
