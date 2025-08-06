import { TestBed } from '@angular/core/testing';

import { WorkStyleService } from './work-style.service';

describe('WorkStyleService', () => {
  let service: WorkStyleService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkStyleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
