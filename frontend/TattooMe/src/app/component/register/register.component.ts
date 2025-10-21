import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { RegisterRequest } from '../../model/register-request';
import { AbstractControl, FormBuilder, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import { NotificationService } from '../../service/notification.service';

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
  loading = false;
  form: FormGroup;
  selectedRole: Role = 'user';

  constructor(
    private authService: AuthService,
    private formBuilder: FormBuilder,
    private notification: NotificationService
  ) {

    this.form = this.formBuilder.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(25)]],
      email: ['', [Validators.required, Validators.email, Validators.maxLength(254)]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.maxLength(50),
          this.passwordStrengthValidator
        ]
      ],
      confirmPassword: ['', [Validators.required]]
    }, {
      validator: this.passwordsMatchValidator
    });
  }

  selectRole(role: Role) {
    this.selectedRole = role;
  }

  passwordStrengthValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    const hasUpperCase = /[A-Z]+/.test(value);
    const hasLowerCase = /[a-z]+/.test(value);
    const hasNumber = /[0-9]+/.test(value);
    const hasSpecialChar = /[!@#$%^&*(){}|,.?":<>]+/.test(value);
    const hasNoSpaces = !/\s/.test(value);

    const passwordValid = hasUpperCase && hasLowerCase && hasNumber && hasSpecialChar && hasNoSpaces;

    return !passwordValid
      ? { weakPassword: true }
      : null;
  }

  passwordsMatchValidator(group: FormGroup) {
    const password = group.get('password')!.value;
    const confirm = group.get('confirmPassword')!.value;
    return password === confirm ? null : { notMatching: true };
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.loading = true;

    const { username, email, password } = this.form.value;

    this.registerRequest = { nickname: username, email, password, role: this.selectedRole };

    this.authService.register(this.registerRequest).subscribe({
      next: response => {
        this.notification.showInfo("Rejestracja udana");
        this.form.reset();
        this.loading = false;
      },
      error: err => {
        this.notification.showError("Błąd rejestracji", err);
        this.form.get('password')?.reset();
        this.form.get('confirmPassword')?.reset();

        this.loading = false;
      }
    });
  }
}
