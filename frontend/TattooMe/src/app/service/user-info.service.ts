import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {
  private apiUrl = 'http://localhost:8080/api/personinfo';

  constructor(private http: HttpClient) {}

  getInfo(): Observable<any> {
    return this.http.get(`${this.apiUrl}/me`);
  }

  updateInfo(info: any): Observable<any> {
    return this.http.put(`${this.apiUrl}`, info);
  }
}
