import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { RegisterRequest } from '../../model/register-request';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

type Role = 'user' | 'trainee' | 'tattoo_artist';

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
  form: FormGroup;
  selectedRole: Role = 'user';
  constructor(private authService: AuthService, private formBuilder: FormBuilder) { 
    this.form = this.formBuilder.group({
      username: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]]
    }, {
      validator: this.passwordsMatchValidator
    });
  }
  selectRole(role: Role) {
    this.selectedRole = role;
  }

  passwordsMatchValidator(group: FormGroup) {
    const password = group.get('password')!.value;
    const confirm = group.get('confirmPassword')!.value;
    return password === confirm ? null : { notMatching: true };
  }
  private roleMap: Record<Role, string> = {
    user: 'user',
    trainee: 'trainee',
    tattoo_artist: 'tattoo_artist'
  };

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.registerRequest = {
      nickname: this.form.value.username,
      email: this.form.value.email,
      password: this.form.value.password,
      role: this.roleMap[this.selectedRole]
    }
    console.log(this.registerRequest);

    this.authService.register(this.registerRequest).subscribe(response => {
      console.log("Powiodlo sie", response);
    },
  error => {
    console.error('Błąd rejestracji:', error);
  })
    
  }
}
