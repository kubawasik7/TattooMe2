import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateSlot } from '../model/create-slot';
import { ScheduleSlot } from '../model/schedule-slot';

@Injectable({
  providedIn: 'root'
})
export class ArtistDateService {
  private url = 'http://localhost:8080/api/schedule';

  constructor(private http: HttpClient) { }

  getSlots(): Observable<ScheduleSlot[]> {
    return this.http.get<ScheduleSlot[]>(this.url);
  }

  getAvailableDates(artistId: string): Observable<ScheduleSlot[]> {
    return this.http.get<ScheduleSlot[]>(`${this.url}/available?artistId=${artistId}`);
  }

  createSlot(slot: CreateSlot): Observable<ScheduleSlot> {
    return this.http.post<ScheduleSlot>(this.url, slot);
  }
  
  deleteSlot(id: string): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  toggleSlot(id: string): Observable<ScheduleSlot> {
    return this.http.put<ScheduleSlot>(`${this.url}/${id}/toggle`, {});
  }
}
