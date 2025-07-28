import { TestBed } from '@angular/core/testing';

import { ArtistDateService } from './artist-date.service';

describe('ArtistDateService', () => {
  let service: ArtistDateService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ArtistDateService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
