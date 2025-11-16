import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class FilterSortSearchService {

  applyFilterSort<T>(
    items: T[],
    searchText: string,
    filterCity: string,
    sortOption: string
  ): T[] {
    let res = [...items];

    // search
    if (searchText.trim().length > 0) {
      const search = searchText.toLowerCase();
      res = res.filter((item: any) =>
        (item.name?.toLowerCase().includes(search)) ||
        (item.nickname?.toLowerCase().includes(search)) ||
        (item.description?.toLowerCase().includes(search)) ||
        (item.city?.toLowerCase().includes(search))
      );
    }

    // filtr miasta
    if (filterCity.trim().length > 0) {
      const city = filterCity.toLowerCase();
      res = res.filter((item: any) => item.city?.toLowerCase().includes(city));
    }

    // sort
    switch (sortOption) {
      case 'name-asc':
        res.sort((a: any, b: any) => {
          const aName = a.name ?? a.nickname ?? '';
          const bName = b.name ?? b.nickname ?? '';
          return aName.localeCompare(bName);
        });
        break;
      case 'name-desc':
        res.sort((a: any, b: any) => {
          const aName = a.name ?? a.nickname ?? '';
          const bName = b.name ?? b.nickname ?? '';
          return bName.localeCompare(aName);
        });
        break;
      case 'rate-desc':
        res.sort((a: any, b: any) => (b.averageRate ?? 0) - (a.averageRate ?? 0));
        break;
      case 'rate-asc':
        res.sort((a: any, b: any) => (a.averageRate ?? 0) - (b.averageRate ?? 0));
        break;
      case 'reviews-desc':
        res.sort((a: any, b: any) => (b.reviewsCount ?? 0) - (a.reviewsCount ?? 0));
        break;
      case 'reviews-asc':
        res.sort((a: any, b: any) => (a.reviewsCount ?? 0) - (b.reviewsCount ?? 0));
        break;
    }

    return res;
  }
}
