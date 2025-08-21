import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { NewVisit } from '../model/new-visit';

@Injectable({
  providedIn: 'root'
})
export class VisitService {
  private apiUrl = 'http://localhost:8080/api/visits';

  constructor(private http: HttpClient) { }
  
  createVisit(dto: NewVisit): Observable<void> {
    return this.http.post<void>(this.apiUrl, dto);
  }
}
