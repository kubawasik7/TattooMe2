import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TattooStudioOffer } from '../model/tattoo-studio-offer';

@Injectable({
  providedIn: 'root'
})
export class TattooStudioOfferService {

private url = 'http://localhost:8080/api/studio-offers';

  constructor(private http: HttpClient) {}

  getCombinedOffers(studioId: string): Observable<TattooStudioOffer[]> {
    return this.http.get<TattooStudioOffer[]>(`${this.url}/${studioId}/combined`);
  }

  createOffer(studioId: string, offer: Partial<TattooStudioOffer>): Observable<TattooStudioOffer> {
    return this.http.post<TattooStudioOffer>(`${this.url}/${studioId}`, offer);
  }

  updateOffer(studioId: string, offerId: string, offer: Partial<TattooStudioOffer>): Observable<TattooStudioOffer> {
    return this.http.put<TattooStudioOffer>(`${this.url}/${studioId}/${offerId}`, offer);
  }

  deleteOffer(studioId: string, offerId: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${studioId}/${offerId}`);
  }
}