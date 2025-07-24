import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {
 private baseUrl = 'http://localhost:8080/api/users';
 private baseUrlOffer = 'http://localhost:8080/api/offers';


  constructor(private http: HttpClient) {}

uploadAvatar(file: File): Observable<void> {
  const formData = new FormData();
  formData.append('avatar', file);

  const token = localStorage.getItem('token');
  const headers = new HttpHeaders({
    'Authorization': `Bearer ${token}`
  });

  return this.http.post<void>(
    `${this.baseUrl}/avatar`,
    formData,
    { headers }
  );
}
updateDescription(description: string): Observable<User> {
    return this.http.put<User>(
      `${this.baseUrl}/description`,
      { description }
    );
  }
}
