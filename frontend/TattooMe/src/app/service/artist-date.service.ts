import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateSlot } from '../model/create-slot';
import { ScheduleSlot } from '../model/schedule-slot';

@Injectable({
  providedIn: 'root'
})
export class ArtistDateService {
  private base = 'http://localhost:8080/api/schedule';

  constructor(private http: HttpClient) { }

  getSlots(): Observable<ScheduleSlot[]> {
    return this.http.get<ScheduleSlot[]>(this.base);
  }

  getAvailableDates(artistId: string): Observable<ScheduleSlot[]> {
    return this.http.get<ScheduleSlot[]>(`${this.base}/available?artistId=${artistId}`);
  }

  createSlot(o: CreateSlot): Observable<ScheduleSlot> {
    return this.http.post<ScheduleSlot>(this.base, o);
  }
  
  deleteSlot(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }

  toggleSlot(id: string): Observable<ScheduleSlot> {
    return this.http.put<ScheduleSlot>(`${this.base}/${id}/toggle`, {});
  }
}
