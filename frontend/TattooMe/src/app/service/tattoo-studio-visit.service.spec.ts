import { TestBed } from '@angular/core/testing';

import { TattooStudioVisitService } from './tattoo-studio-visit.service';

describe('TattooStudioVisitService', () => {
  let service: TattooStudioVisitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TattooStudioVisitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
