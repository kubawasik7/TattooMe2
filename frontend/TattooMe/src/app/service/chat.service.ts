import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Chat } from '../model/chat';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private baseUrl = 'http://localhost:8080/api/chats';

  constructor(private http: HttpClient) { }

  getUserChats(): Observable<Chat[]> {
    return this.http.get<Chat[]>(this.baseUrl);
  }
  
  startChat(receiverId: string): Observable<Chat> {
    const params = new HttpParams().set('receiverId', receiverId);
    return this.http.post<Chat>(`${this.baseUrl}/start`, null, { params });
  }
}
