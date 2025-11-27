import { Injectable } from '@angular/core';
import { TattooStyle } from '../model/tattoo-style';
import { Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class WorkStyleService {
  private url = 'http://localhost:8080/api/styles';

  constructor(private http: HttpClient) {}

  getAllStyles(): Observable<TattooStyle[]> {
    return this.http.get<TattooStyle[]>(`${this.url}/all`);
  }

  getUserStyles(userId: string): Observable<TattooStyle[]> {
    return this.http.get<TattooStyle[]>(`${this.url}/user/${userId}`);
  }
  
  saveUserStyles(userId: string, styleIds: string[]): Observable<void> {
    return this.http.post<void>(`${this.url}/user/${userId}`, styleIds);
  }
}
