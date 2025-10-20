import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../service/user.service';
import { User } from '../../model/user';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NotificationService } from '../../service/notification.service';

@Component({
  selector: 'app-user-profile',
  standalone: false,
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent {
  user!: User;
  userForm!: FormGroup;
  editMode = false;
  selectedFile: any;

  constructor(private route: ActivatedRoute,
    private userService: UserService,
    private fb: FormBuilder,
    private notification: NotificationService
  ) { }

  ngOnInit() {
    const userId = this.route.snapshot.paramMap.get('id')!;
    this.userService.getUserById(userId).subscribe((data: User) => {
      this.user = data;
      this.initializeForm();
    });
  }

  initializeForm(): void {
    this.userForm = this.fb.group({
      nickname: [this.user.nickname, [Validators.required, Validators.minLength(3), Validators.maxLength(25)]],
      name: [this.user.name, [Validators.minLength(3), Validators.maxLength(35)]],
      surname: [this.user.surname, [Validators.minLength(3), Validators.maxLength(60)]],
      email: [this.user.email, [Validators.required, Validators.email, Validators.maxLength(254)]]
    });
  }

  get nickname() { return this.userForm?.get('nickname')!; }
  get name() { return this.userForm?.get('name')!; }
  get surname() { return this.userForm?.get('surname')!; }
  get email() { return this.userForm?.get('email')!; }

  toggleEdit(): void {
    this.editMode = true;
  }

  cancelChanges(): void {
    this.userForm.patchValue(this.user);
    this.editMode = false;
  }

  saveChanges(): void {
    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    const updatedUser = { ...this.user, ...this.userForm.value };
    this.userService.updateUser(updatedUser).subscribe({
      next: () => {
        this.user = updatedUser;
        this.editMode = false;
        this.notification.showSuccess("Informacje zostały zaktualizowane");
      },
      error: (err) => this.notification.showError("Nie udało się zaktualizować", err)
    });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }
}
