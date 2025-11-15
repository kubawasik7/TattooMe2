import { Component } from '@angular/core';
import { LoginRequest } from '../../model/login-request';
import { AuthService } from '../../service/auth.service';
import { Router } from '@angular/router';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { NotificationService } from '../../service/notification.service';
import Swal from 'sweetalert2';

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
        Swal.fire({
          icon: 'success',
          background: '#1e1e1e',
          color: '#ffffff',
          title: 'Logowanie udane',
          timer: 1500,
          showConfirmButton: false
        }).then(() => {
          this.router.navigate(['/dashboard']);
        });
        localStorage.setItem('token', response.token);
      },
      error => {
        this.notification.showError("Błąd logowania");
      }
    );
  }
}
