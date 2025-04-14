import { Component } from '@angular/core';
import { LoginRequest } from '../../model/login-request';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginRequest: LoginRequest = {
    nickname: '',
    password: ''
  };
  constructor(private authService: AuthService, private router: Router) { }

  onLogin(): void {
    this.authService.login(this.loginRequest).subscribe(
      response => {
        console.log('Logowanie powiodło się:', response);
        localStorage.setItem('token', response.token);
        this.router.navigate(['/dashboard']);
      },
      error => {
        console.error('Błąd logowania:', error);
      }
    );
  }
}
