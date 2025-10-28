import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateTattoStudio } from '../model/create-tattoo-studio';
import { TattooStudio } from '../model/tattoo-studio';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class StudioService {
  private baseUrl = 'http://localhost:8080/api/studios';

  constructor(private http: HttpClient) { }

  getStudioById(id: string): Observable<TattooStudio> {
    return this.http.get<TattooStudio>(`${this.baseUrl}/${id}`);
  }

  getUsersByStudioId(studioId: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/${studioId}/users`);
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
}
