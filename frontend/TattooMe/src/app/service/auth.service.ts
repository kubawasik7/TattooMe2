import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { RegisterRequest } from '../model/register-request';
import { LoginRequest } from '../model/login-request';
import { LoginResponse } from '../model/login-response';
import { jwtDecode } from 'jwt-decode';
interface JwtPayload {
  sub: string;
  email?: string;
  username?: string;
  role?: string;
  exp?: number;
}
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private url = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) { }

  getToken(): string | null {
    return (typeof window !== 'undefined') ? localStorage.getItem('token') : null;
  }

  getNickname(): string | null {
    const token = this.getToken();
    if (!token) return null;

    try {
      const decoded = jwtDecode<JwtPayload>(token);
      return decoded.username || null;
    } catch (e) {
      return null;
    }
  }

  getUserRole(): string | null {
    const token = this.getToken();
    if (!token) return null;

    const decodedToken: any = jwtDecode(token);
    return decodedToken.role;
  }

  getUserId(): string | null {
    const token = this.getToken();
    if (!token) return null;

    const { sub } = jwtDecode<JwtPayload & { sub?: string }>(token);
    return sub ?? null;
  }

  isTattooArtist(): boolean | null {
    const token = this.getToken();
    if (!token) return null;
    const decodedToken: any = jwtDecode(token);

    if (decodedToken.role === 'tattoo_artist') {
      return true;
    }
    return false;
  }

  isTrainee(): boolean | null {
    const token = this.getToken();
    if (!token) return null;
    const decodedToken: any = jwtDecode(token);

    if (decodedToken.role === 'trainee') {
      return true;
    }
    return false;
  }
  isClient(): boolean | null {
    const token = this.getToken();
    if (!token) return null;
    const decodedToken: any = jwtDecode(token);

    if (decodedToken.role === 'user') {
      return true;
    }
    return false;
  }

  register(registerRequest: RegisterRequest): Observable<any> {
    return this.http.post(`${this.url}/register`, registerRequest);
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.url}/login`, loginRequest)
      .pipe(tap(res => {
        console.log('[AuthService] otrzymany response:', res);
        console.log('[AuthService] otrzymany token:', res.token);
        localStorage.setItem('token', res.token)
      }));
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  logout(): void {
    localStorage.removeItem('token');
  }
}


