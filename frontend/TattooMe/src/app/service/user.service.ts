import { HttpClient } from '@angular/common/http';
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
  getUsers() : Observable<User[]>{
    return this.http.get<User[]>(this.apiUrl);
  }
  getUserById(id: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }
  getTattooArtists() : Observable<User[]>{
    return this.http.get<User[]>(`${this.apiUrl}/tattooArtist`);
  }
  getTrainees() : Observable<User[]>{
    return this.http.get<User[]>(`${this.apiUrl}/trainee`);
  }
   updateUser(user: User): Observable<any> {
    return this.http.put(`${this.apiUrl}/userProfile`, user);
  }
}
