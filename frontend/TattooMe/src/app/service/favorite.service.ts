import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FavoriteArtist } from '../model/favorite-artist';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class FavoriteService {
  private url = 'http://localhost:8080/api/favorites';

  constructor(private http: HttpClient) {}

  getFavorites(): Observable<User[]> {
    return this.http.get<User[]>(this.url);
  }

  addFavorite(artistId: string): Observable<void> {
    return this.http.post<void>(`${this.url}/${artistId}`, {});
  }

  removeFavorite(artistId: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${artistId}`);
  }
}