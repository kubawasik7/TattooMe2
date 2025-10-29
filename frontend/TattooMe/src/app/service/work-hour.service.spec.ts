import { TestBed } from '@angular/core/testing';

import { WorkHourService } from './work-hour.service';

describe('WorkHourService', () => {
  let service: WorkHourService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WorkHourService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
