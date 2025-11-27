import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Flash } from '../model/flash';

@Injectable({
  providedIn: 'root'
})
export class FlashService {
  private url = 'http://localhost:8080/api/flashes';

  constructor(private http: HttpClient) { }
  getByUser(userId: string): Observable<Flash[]> {
    return this.http.get<Flash[]>(`${this.url}/${userId}`);
  }

  getFlashesFromStudio(studioId: string) {
    return this.http.get<Flash[]>(`${this.url}/studio/${studioId}`);
  }

  upload(formData: FormData): Observable<void> {
    return this.http.post<void>(`${this.url}/upload`, formData);
  }
}
