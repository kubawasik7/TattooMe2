import { TestBed } from '@angular/core/testing';

import { TattooStudioOfferService } from './tattoo-studio-offer.service';

describe('TattooStudioOfferService', () => {
  let service: TattooStudioOfferService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TattooStudioOfferService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
