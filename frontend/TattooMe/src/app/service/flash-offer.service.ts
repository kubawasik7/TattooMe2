import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FlashOffer } from '../model/flash-offer';

@Injectable({
  providedIn: 'root'
})

export class FlashOfferService {

  private url = 'http://localhost:8080/api/flash-offer';

  constructor(private http: HttpClient) {}

  getByArtistOffer(offerId: string): Observable<FlashOffer[]> {
    return this.http.get<FlashOffer[]>(`${this.url}/artist-offer/${offerId}`);
  }

  create(dto: FlashOffer): Observable<FlashOffer> {
    return this.http.post<FlashOffer>(this.url, dto);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}