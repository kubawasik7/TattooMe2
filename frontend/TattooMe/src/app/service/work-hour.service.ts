import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { WorkHour } from '../model/work-hour';

@Injectable({
  providedIn: 'root'
})
export class WorkHourService {
  private url = 'http://localhost:8080/api/studios';

  constructor(private http: HttpClient) { }

  getWorkHours(studioId: string): Observable<WorkHour[]> {
    return this.http.get<WorkHour[]>(`${this.url}/${studioId}/work-hours`);
  }

  addWorkHour(studioId: string, payload: Partial<WorkHour>): Observable<WorkHour> {
    return this.http.post<WorkHour>(`${this.url}/${studioId}/work-hours`, payload);
  }

  updateWorkHour(workHourId: string, payload: Partial<WorkHour>): Observable<WorkHour> {
    return this.http.put<WorkHour>(`${this.url}/work-hours/${workHourId}`, payload);
  }

  deleteWorkHour(studioId: string, workHourId: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${studioId}/work-hours/${workHourId}`);
  }
}