import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private url = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) { }

  getUsersByRole(role: 'tattoo_artist' | 'trainee'): Observable<User[]> {
    const params = new HttpParams().set('role', role);
    return this.http.get<User[]>(this.url, { params });
  }
  
  getUsersByRoleAVG(): Observable<User[]> {
    return this.http.get<User[]>(`${this.url}/top`);
  }

  getUserById(id: string): Observable<User> {
    return this.http.get<User>(`${this.url}/${id}`);
  }

  updateUser(user: User): Observable<any> {
    return this.http.put(`${this.url}/userProfile`, user);
  }
}
