import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'TattooMe';
  constructor(private router: Router) { }

  onLoginClick(): void {
    console.log('Przycisk logowania został kliknięty.');
        this.router.navigate(['/login']);
  }

  
  navigateTo(path: string): void {
    this.router.navigate([`/${path}`]);
  }
}
