import { Component, OnInit } from '@angular/core';
import { UserInfoService } from '../../service/user-info.service';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-user-info',
  standalone: false,
  templateUrl: './user-info.component.html',
  styleUrl: './user-info.component.css'
})
export class UserInfoComponent implements OnInit {
  infoForm!: FormGroup;
  editMode = false;
  originalInfo: any;

  constructor(private userInfoService: UserInfoService, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.infoForm = this.fb.group({
      allergies: [{ value: '', disabled: true }, [Validators.maxLength(255)]],
      chronicDiseases: [{ value: '', disabled: true }, [Validators.maxLength(255)]],
      medicines: [{ value: '', disabled: true }, [Validators.maxLength(255)]],
      experiences: [{ value: '', disabled: true }, [Validators.maxLength(255)]]
    });

    this.loadInfo();
  }

  loadInfo() {
    this.userInfoService.getInfo().subscribe({
      next: (data) => {
        this.originalInfo = { ...data };
        this.infoForm.patchValue(data);
      },
      error: (err) => {
        console.error('Błąd podczas pobierania danych', err);
      }
    });
  }

  enableEdit() {
    this.editMode = true;
    this.infoForm.enable();
  }

  cancelChanges() {
    this.editMode = false;
    this.infoForm.disable();
    this.infoForm.patchValue(this.originalInfo);
  }

  saveChanges() {
    if (this.infoForm.invalid) {
      this.infoForm.markAllAsTouched();
      return;
    }

    const formData = this.infoForm.value;

    this.userInfoService.updateInfo(formData).subscribe({
      next: (updatedInfo) => {
        this.originalInfo = { ...updatedInfo };
        this.infoForm.patchValue(updatedInfo);
        this.infoForm.disable();
        this.editMode = false;

      },
      error: (err) => {
        console.error('Błąd przy zapisie:', err);
      }
    });
  }
}