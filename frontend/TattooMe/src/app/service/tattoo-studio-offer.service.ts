import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TattooStudioOffer } from '../model/tattoo-studio-offer';

@Injectable({
  providedIn: 'root'
})
export class TattooStudioOfferService {

private api = 'http://localhost:8080/api/studio-offers';

  constructor(private http: HttpClient) {}

  // Pobranie wszystkich promocji (studio + artyści)
  getCombinedOffers(studioId: string): Observable<TattooStudioOffer[]> {
    return this.http.get<TattooStudioOffer[]>(`${this.api}/${studioId}/combined`);
  }

  // Dodanie promocji studia
  createOffer(studioId: string, offer: Partial<TattooStudioOffer>): Observable<TattooStudioOffer> {
    return this.http.post<TattooStudioOffer>(`${this.api}/${studioId}`, offer);
  }

  // Edycja promocji
  updateOffer(studioId: string, offerId: string, offer: Partial<TattooStudioOffer>): Observable<TattooStudioOffer> {
    return this.http.put<TattooStudioOffer>(`${this.api}/${studioId}/${offerId}`, offer);
  }

  // Usunięcie promocji
  deleteOffer(studioId: string, offerId: string): Observable<void> {
    return this.http.delete<void>(`${this.api}/${studioId}/${offerId}`);
  }
}