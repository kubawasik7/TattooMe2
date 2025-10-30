import { Component, EventEmitter, Input, Output } from '@angular/core';
import { User } from '../../../model/user';
import { StudioService } from '../../../service/studio.service';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../service/auth.service';
import { StudioArtist } from '../../../model/studio-artist';

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
    private authService: AuthService
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
      error: err => alert('Błąd dodawania użytkownika: ' + err.error.message)
    });
  }

  removeUser(userId: string): void {
    if (confirm('Czy na pewno chcesz usunąć tego użytkownika ze studia?')) {
      this.studioService.removeUserFromStudio(this.studioId, userId).subscribe({
        next: () => this.membersUpdated.emit(),
        error: err => alert('Błąd podczas usuwania: ' + err.error.message)
      });
    }
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
        alert('Błąd przy zmianie uprawnień: ' + err.error.message);
        this.loading = false;
      }
    });
  }
}

