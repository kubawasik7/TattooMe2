import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { RegisterRequest } from '../../model/register-request';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  registerRequest: RegisterRequest = {
    nickname: '',
    email: '',
    password: '',
    role: ''
  };
  constructor(private authService: AuthService) { }

  onRegister(): void {
    this.authService.register(this.registerRequest).subscribe(
      response => {
        console.log('Rejestracja powiodła się:', response);
      },
      error => {
        console.error('Błąd rejestracji:', error);
      }
    );
  }
}
