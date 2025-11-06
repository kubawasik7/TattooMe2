import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateTattoStudio } from '../model/create-tattoo-studio';
import { TattooStudio } from '../model/tattoo-studio';
import { User } from '../model/user';
import { StudioArtist } from '../model/studio-artist';
import { StudioSchedule } from '../model/studio-schedule';

@Injectable({
  providedIn: 'root'
})
export class StudioService {
  private baseUrl = 'http://localhost:8080/api/studios';

  constructor(private http: HttpClient) { }

  getStudioById(id: string): Observable<TattooStudio> {
    return this.http.get<TattooStudio>(`${this.baseUrl}/${id}`);
  }

  getUsersByStudioId(studioId: string): Observable<StudioArtist[]> {
    return this.http.get<StudioArtist[]>(`${this.baseUrl}/${studioId}/users`);
  }
  
  getAllArtistSlots(studioId: string): Observable<StudioSchedule[]> {
    return this.http.get<StudioSchedule[]>(`${this.baseUrl}/${studioId}/slots`);
  }

  addUserToStudio(studioId: string, nickname: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/${studioId}/users/by-nickname?nickname=${nickname}`, {});
  }

  removeUserFromStudio(studioId: string, userId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${studioId}/users/${userId}`);
  }

  getStudios(): Observable<TattooStudio[]> {
    return this.http.get<TattooStudio[]>(this.baseUrl);
  }

  createStudio(dto: CreateTattoStudio): Observable<string> {
    return this.http.post<string>(this.baseUrl, dto);
  }

  updateMemberRole(studioId: string, userId: string, role: string): Observable<any> {
    const body = { role };
    return this.http.put(`${this.baseUrl}/${studioId}/members/${userId}/role`, body);
  }
}
