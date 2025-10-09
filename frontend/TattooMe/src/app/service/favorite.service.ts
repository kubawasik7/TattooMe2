import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FavoriteArtist } from '../model/favorite-artist';

@Injectable({
  providedIn: 'root'
})
export class FavoriteService {
  private baseUrl = 'http://localhost:8080/api/favorites';

  constructor(private http: HttpClient) {}

  getFavorites(): Observable<FavoriteArtist[]> {
    return this.http.get<FavoriteArtist[]>(this.baseUrl);
  }

  addFavorite(artistId: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${artistId}`, {});
  }

  removeFavorite(artistId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${artistId}`);
  }
}