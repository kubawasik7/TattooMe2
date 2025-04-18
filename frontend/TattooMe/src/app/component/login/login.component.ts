import { Component } from '@angular/core';
import { LoginRequest } from '../../model/login-request';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { FormGroup,FormBuilder, Validators } from '@angular/forms';

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
  form: FormGroup;

  constructor(private authService: AuthService, private router: Router, private formBuilder: FormBuilder) {
    this.form = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required]]
    })
   }

  onLogin(): void {
    this.loginRequest = {
      nickname: this.form.value.username,
      password: this.form.value.password
    }
    this.authService.login(this.loginRequest).subscribe(
      response => {
        console.log('Logowanie powiodło się:', response);
        // Zapisz token np. w localStorage, aby później używać przy kolejnych zapytaniach
        localStorage.setItem('token', response.token);
        // Przekierowanie po zalogowaniu
        this.router.navigate(['/dashboard']);
      },
      error => {
        console.error('Błąd logowania:', error);
      }
    );
  }
}
