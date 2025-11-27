import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user';


export interface Offer {
  id: string;
  startDate: string;
  endDate: string;
  description: string;
}

export interface CreateOffer {
  startDate: string;
  endDate: string;
  description: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
  private url = 'http://localhost:8080/api/users';
  private urlOffer = 'http://localhost:8080/api/offers';


  constructor(private http: HttpClient) { }

  uploadAvatar(file: File): Observable<void> {
    const formData = new FormData();
    formData.append('avatar', file);

    const token = localStorage.getItem('token');
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post<void>(
      `${this.url}/avatar`,
      formData,
      { headers }
    );
  }

  updateDescription(description: string): Observable<User> {
    return this.http.put<User>(
      `${this.url}/description`,
      { description }
    );
  }

  //SEKCJA OFERT
  getOffers(id: string): Observable<Offer[]> {
    return this.http.get<Offer[]>(`${this.urlOffer}/${id}`);
  }

  createOffer(o: CreateOffer): Observable<Offer> {
    return this.http.post<Offer>(`${this.urlOffer}`, o);
  }

  updateOffer(id: string, o: CreateOffer): Observable<Offer> {
    return this.http.put<Offer>(`${this.urlOffer}/${id}`, o);
  }

  deleteOffer(id: string): Observable<void> {
    return this.http.delete<void>(`${this.urlOffer}/${id}`);
  }
}
