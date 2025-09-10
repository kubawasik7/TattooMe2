import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NewVisit } from '../model/new-visit';
import { Visit } from '../model/visit';
export const VISIT_STATUS = {
  PENDING: 'OCZEKUJĄCA' as const,
  APPROVED: 'ZATWIERDZONA' as const,
  CANCELLED: 'ANULOWANA' as const,
};
export type VisitStatusValue = typeof VISIT_STATUS[keyof typeof VISIT_STATUS];

@Injectable({
  providedIn: 'root'
})
export class VisitService {
  private apiUrl = 'http://localhost:8080/api/visits';

  constructor(private http: HttpClient) { }
  
  createVisit(dto: NewVisit): Observable<void> {
    return this.http.post<void>(this.apiUrl, dto);
  }
   getMyVisits(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.apiUrl}/my`);
  }
  getActive(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.apiUrl}/active`);
  }

  getPast(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.apiUrl}/past`);
  }

  getCancelled(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.apiUrl}/cancelled`);
  }
  getActiveAsArtist(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.apiUrl}/artist/active`);
  }

  getPastAsArtist(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.apiUrl}/artist/past`);
  }

  getCancelledAsArtist(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.apiUrl}/artist/cancelled`);
  }

  confirmVisit(id: string): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/confirm`, {});
  }

  cancelVisit(id: string): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/cancel`, {});
  }
  getById(id: string): Observable<Visit> {
    return this.http.get<Visit>(`${this.apiUrl}/${id}`);
  }



}
