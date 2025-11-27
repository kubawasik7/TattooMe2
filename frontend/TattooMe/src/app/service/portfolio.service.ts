import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Portfolio } from '../model/portfolio';

@Injectable({
  providedIn: 'root'
})
export class PortfolioService {
  private url = 'http://localhost:8080/api/portfolio';

  constructor(private http: HttpClient) { }

  getByUser(userId: string): Observable<Portfolio[]> {
    return this.http.get<Portfolio[]>(`${this.url}/${userId}`);
  }

  uploadImage(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('file', file);
    return this.http.post<Portfolio>(`${this.url}/upload`, formData);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  updateFeatured(userId: string, itemId: string, featured: boolean): Observable<any> {
    return this.http.patch(`${this.url}/user/${userId}/item/${itemId}/featured`, { featured });
  }
}
