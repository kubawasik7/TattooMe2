import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { WorkHour } from '../model/work-hour';

@Injectable({
  providedIn: 'root'
})
export class WorkHourService {
  private baseUrl = 'http://localhost:8080/api/studios';

  constructor(private http: HttpClient) { }

  getWorkHours(studioId: string): Observable<WorkHour[]> {
    return this.http.get<WorkHour[]>(`${this.baseUrl}/${studioId}/work-hours`);
  }

  addWorkHour(studioId: string, payload: Partial<WorkHour>): Observable<WorkHour> {
    return this.http.post<WorkHour>(`${this.baseUrl}/${studioId}/work-hours`, payload);
  }

  updateWorkHour(workHourId: string, payload: Partial<WorkHour>): Observable<WorkHour> {
    return this.http.put<WorkHour>(`${this.baseUrl}/work-hours/${workHourId}`, payload);
  }

  deleteWorkHour(studioId: string, workHourId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${studioId}/work-hours/${workHourId}`);
  }
}