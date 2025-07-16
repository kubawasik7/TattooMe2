import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './service/auth.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'TattooMe';
  constructor(private router: Router, public authService: AuthService) { }

  onLoginClick(): void {
    console.log('Przycisk logowania został kliknięty.');
        this.router.navigate(['/login']);
  }

  
  navigateTo(path: string): void {
    this.router.navigate([`/${path}`]);
  }
   onLogout(): void {
    this.authService.logout();
    console.log('logout');
  }
}
