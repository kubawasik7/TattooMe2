import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StudioVisit } from '../model/studio-visit';

@Injectable({
  providedIn: 'root'
})
export class TattooStudioVisitService {
  private url = 'http://localhost:8080/api/studio-visits';

  constructor(private http: HttpClient) {}

  createVisit(studioId: string, visit: any): Observable<any> {
    return this.http.post(`${this.url}/${studioId}`, visit);
  }
    getById(id: string): Observable<StudioVisit> {
    return this.http.get<StudioVisit>(`${this.url}/${id}`);
  }

  getActive(): Observable<StudioVisit[]> {
    return this.http.get<StudioVisit[]>(`${this.url}/studio/active`);
  }

  getPast(): Observable<StudioVisit[]> {
    return this.http.get<StudioVisit[]>(`${this.url}/studio/past`);
  }

  getCancelled(): Observable<StudioVisit[]> {
    return this.http.get<StudioVisit[]>(`${this.url}/studio/cancelled`);
  }

  confirmVisit(id: string): Observable<void> {
    return this.http.patch<void>(`${this.url}/${id}/confirm`, {});
  }

  cancelVisit(visitId: string): Observable<void> {
    return this.http.patch<void>(`${this.url}/${visitId}/cancel`, {});
  }
}