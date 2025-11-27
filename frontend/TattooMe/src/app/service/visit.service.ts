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
  private url = 'http://localhost:8080/api/visits';

  constructor(private http: HttpClient) { }

  getById(id: string): Observable<Visit> {
    return this.http.get<Visit>(`${this.url}/${id}`);
  }

  getMyVisits(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.url}/my`);
  }

  getActive(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.url}/active`);
  }

  getPast(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.url}/past`);
  }

  getCancelled(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.url}/cancelled`);
  }

  getActiveAsArtist(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.url}/artist/active`);
  }

  getPastAsArtist(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.url}/artist/past`);
  }

  getCancelledAsArtist(): Observable<Visit[]> {
    return this.http.get<Visit[]>(`${this.url}/artist/cancelled`);
  }

  createVisit(dto: NewVisit): Observable<void> {
    return this.http.post<void>(this.url, dto);
  }

  confirmVisit(id: string): Observable<void> {
    return this.http.patch<void>(`${this.url}/${id}/confirm`, {});
  }

  cancelVisitAsArtist(visitId: string): Observable<void> {
    return this.http.patch<void>(`${this.url}/${visitId}/cancel`, {});
  }

  cancelVisitAsClient(visitId: string): Observable<void> {
    return this.http.patch<void>(`${this.url}/${visitId}/cancel/client`, {});
  }
}
