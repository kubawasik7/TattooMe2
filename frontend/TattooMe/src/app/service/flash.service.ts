import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Flash } from '../model/flash';

@Injectable({
  providedIn: 'root'
})
export class FlashService {
  private baseUrl = 'http://localhost:8080/api/flashes';

  constructor(private http: HttpClient) {}
  getByUser(userId: string): Observable<Flash[]> {
    return this.http.get<Flash[]>(`${this.baseUrl}/${userId}`);
  }

  upload(formData: FormData): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/upload`, formData);
  }
}
