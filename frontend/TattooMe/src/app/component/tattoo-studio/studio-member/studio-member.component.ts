import { Component, EventEmitter, Input, Output } from '@angular/core';
import { User } from '../../../model/user';
import { StudioService } from '../../../service/studio.service';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../service/auth.service';
import { StudioArtist } from '../../../model/studio-artist';
import { NotificationService } from '../../../service/notification.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-studio-member',
  standalone: false,
  templateUrl: './studio-member.component.html',
  styleUrl: './studio-member.component.css'
})
export class StudioMemberComponent {
  @Input() users: StudioArtist[] = [];
  @Input() studioId!: string;
  @Output() membersUpdated = new EventEmitter<void>();
  newUserNickname: string = '';
  loading = false;
  currentUserId: string | null = null;
  currentUserRole: string | null = null;

  constructor(
    private studioService: StudioService,
    private route: ActivatedRoute,
    private authService: AuthService,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.currentUserId = this.authService.getUserId();
    const current = this.users.find(u => u.id === this.currentUserId);
    this.currentUserRole = current?.studioRole || null;
  }

  get currentUserRoleInStudio(): string | null {
    const current = this.users.find(u => u.id === this.currentUserId);
    return current?.studioRole || null;
  }

  addUser(): void {
    if (!this.newUserNickname.trim()) return;
    this.studioService.addUserToStudio(this.studioId, this.newUserNickname).subscribe({
      next: () => {
        this.membersUpdated.emit();
        this.newUserNickname = '';
      },
      error: err => this.notification.showError("Nie udalo sie dodac uzytkownika")
    });
  }

  removeUser(userId: string): void {
    Swal.fire({
      title: 'Czy na pewno chcesz usunąć tego użytkownika?',
      text: 'Tej akcji nie można cofnąć.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      background: '#1e1e1e',
      color: '#ffffffff',
      confirmButtonText: 'Tak, usuń',
      cancelButtonText: 'Anuluj'
    }).then((result) => {
      if (result.isConfirmed) {
        this.studioService.removeUserFromStudio(this.studioId, userId).subscribe({
          next: () => {
            this.membersUpdated.emit()
            Swal.fire({
              icon: 'success',
              background: '#1e1e1e',
              color: '#ffffffff',
              title: 'Usunięto!',
              text: 'Użytkownik został pomyślnie usunięty',
              timer: 1500,
              showConfirmButton: false
            });
          },
          error: (err) => {
            this.notification.showError('Nie udało się usunąć użytkownika');
          }
        });
      }
    });
  }

  togglePermissions(user: StudioArtist): void {
    const newRole = user.studioRole === 'MEMBER' ? 'EDITOR' : 'MEMBER';
    this.loading = true;
    this.studioService.updateMemberRole(this.studioId, user.id, newRole).subscribe({
      next: res => {
        user.studioRole = res.role;
        this.loading = false;
      },
      error: err => {
        this.notification.showError("Błąd przy zmianie uprawnień");
        this.loading = false;
      }
    });
  }
}

