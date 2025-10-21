import { Component } from '@angular/core';
import { LoginRequest } from '../../model/login-request';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NotificationService } from '../../service/notification.service';

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

  constructor(private authService: AuthService,
    private router: Router,
    private formBuilder: FormBuilder,
    private notification: NotificationService
  ) {
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
        this.notification.showInfo("Logowanie się powiodło");
        localStorage.setItem('token', response.token);
        this.router.navigate(['/dashboard']);
      },
      error => {
        this.notification.showError("Błąd logowania", error);
      }
    );
  }
}
