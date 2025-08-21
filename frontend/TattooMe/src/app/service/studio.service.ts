import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CreateTattoStudio } from '../model/create-tattoo-studio';
import { TattooStudio } from '../model/tattoo-studio';

@Injectable({
  providedIn: 'root'
})
export class StudioService {
  private baseUrl = 'http://localhost:8080/api/studios';

  constructor(private http: HttpClient) {}
  
  getStudios(): Observable<TattooStudio[]>{
    return this.http.get<TattooStudio[]>(this.baseUrl);
  }

  createStudio(dto: CreateTattoStudio): Observable<string> {
    return this.http.post<string>(this.baseUrl, dto);
  }
}
