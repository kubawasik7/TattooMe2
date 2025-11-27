import { Component } from '@angular/core';
import { AuthService } from './service/auth.service';
import { jwtDecode } from 'jwt-decode';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'TattooMe';
  constructor(public authService: AuthService) { }

  onLogout(): void {
    this.authService.logout();
  }

  getUserId(): string | null {
    const token = localStorage.getItem('token');
    if (!token) return null;

    const decoded: any = jwtDecode(token);
    return decoded.sub;
  }
}
