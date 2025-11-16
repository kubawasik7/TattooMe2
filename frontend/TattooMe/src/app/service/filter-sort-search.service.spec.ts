import { TestBed } from '@angular/core/testing';

import { FilterSortSearchService } from './filter-sort-search.service';

describe('FilterSortSearchService', () => {
  let service: FilterSortSearchService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FilterSortSearchService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
