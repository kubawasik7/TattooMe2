import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Message } from '../model/message';
import { NewMessage } from '../model/new-message';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private baseUrl = 'http://localhost:8080/api/messages';

  constructor(private http: HttpClient) {}

  getMessages(chatId: string): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.baseUrl}/${chatId}`);
  }

  sendMessage(message: NewMessage): Observable<void> {
    return this.http.post<void>(this.baseUrl, message);
  }
}
