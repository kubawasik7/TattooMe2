import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserInfoService {
  private url = 'http://localhost:8080/api/personinfo';

  constructor(private http: HttpClient) {}

  getInfo(): Observable<any> {
    return this.http.get(`${this.url}/me`);
  }

  updateInfo(info: any): Observable<any> {
    return this.http.put(`${this.url}`, info);
  }
}
