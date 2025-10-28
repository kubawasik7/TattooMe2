import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Faq } from '../model/faq';

@Injectable({
  providedIn: 'root'
})
export class FaqService {
  private baseUrl = 'http://localhost:8080/api/studios';

  constructor(private http: HttpClient) {}

  getFaqs(studioId: string): Observable<Faq[]> {
    return this.http.get<Faq[]>(`${this.baseUrl}/${studioId}/faq`);
  }

  addFaq(studioId: string, faq: Partial<Faq>): Observable<Faq> {
    return this.http.post<Faq>(`${this.baseUrl}/${studioId}/faq`, faq);
  }

  deleteFaq(studioId: string, faqId: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${studioId}/faq/${faqId}`);
  }
}