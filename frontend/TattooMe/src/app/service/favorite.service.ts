import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { User } from './user.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FavoriteService {

  private baseUrl = 'http://localhost:8080/api/favorites';

  constructor(private http: HttpClient) {}

  getFavorites(): Observable<User[]> {
    return this.http.get<User[]>(this.baseUrl);
  }

  addFavorite(artistId: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${artistId}`, {});
  }

  removeFavorite(artistId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${artistId}`);
  }
}