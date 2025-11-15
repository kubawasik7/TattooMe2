import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import Swal from 'sweetalert2';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  showWarning(message: string): void {
    Swal.fire({
      icon: 'warning',
      background: '#1e1e1e',
      color: '#ffffff',
      text: message,
      showConfirmButton: true
    });
  }

  showSuccess(message: string): void {
    Swal.fire({
      icon: 'success',
      background: '#1e1e1e',
      color: '#ffffff',
      text: message,
      showConfirmButton: true
    });
  }

  showError(message: string): void {
    Swal.fire({
      icon: 'error',
      background: '#1e1e1e',
      color: '#ffffff',
      text: message,
      showConfirmButton: true
    });
  }
}
