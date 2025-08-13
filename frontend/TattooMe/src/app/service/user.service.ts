import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UUID } from 'node:crypto';
import { Observable } from 'rxjs';
export interface User{
  id: UUID;
  nickname: string;
  email: string;
  name: string;
  surname: string;
  description: string;
  profilePicture: string | null;
}

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';
  constructor(private http: HttpClient) { }
  getUsersByRole(role: 'tattoo_artist' | 'trainee'): Observable<User[]>{
    const params = new HttpParams().set('role', role);
    return this.http.get<User[]>(this.apiUrl, {params});
  }
  getUserById(id: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }
   updateUser(user: User): Observable<any> {
    return this.http.put(`${this.apiUrl}/userProfile`, user);
  }
}
