import { Component } from '@angular/core';
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
  users: StudioArtist[] = [];
  newUserNickname: string = '';
  studioId: string = '';
  loading = false;
  currentUserId: string | null = null;
  currentUserRole: string | null = null;

  constructor(
    private studioService: StudioService,
    private route: ActivatedRoute,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.studioId = this.route.snapshot.paramMap.get('id')!;
    this.currentUserId = this.authService.getUserId();
    this.loadUsers();
  }

  loadUsers(): void {
    this.studioService.getUsersByStudioId(this.studioId).subscribe({
      next: (data) => {
        this.users = data.map(user => ({
          ...user,
          featuredPictures: user.featuredPictures ?? []
        }));
        const current = this.users.find(u => u.id === this.currentUserId);
        this.currentUserRole = current?.studioRole || null;

        console.log('Twoja rola w studiu:', this.currentUserRole);
      },
      error: err => console.error('Błąd ładowania użytkowników:', err)
    });
  }

  addUser(): void {
    if (!this.newUserNickname.trim()) return;

    this.studioService.addUserToStudio(this.studioId, this.newUserNickname).subscribe({
      next: () => {
        this.loadUsers();
        this.newUserNickname = '';
      },
      error: err => alert('Błąd dodawania użytkownika: ' + err.error.message)
    });
  }

  removeUser(userId: string): void {
    if (confirm('Czy na pewno chcesz usunąć tego użytkownika ze studia?')) {
      this.studioService.removeUserFromStudio(this.studioId, userId).subscribe({
        next: () => this.loadUsers(),
        error: err => alert('Błąd podczas usuwania: ' + err.error.message)
      });
    }
  }

  togglePermissions(user: StudioArtist): void {
    const newRole = user.studioRole === 'MEMBER' ? 'EDITOR' : 'MEMBER';

    this.loading = true;
    this.studioService.updateMemberRole(this.studioId, user.id, newRole).subscribe({
      next: (res) => {
        user.studioRole = res.role;
        this.loading = false;
      },
      error: (err) => {
        alert('Błąd przy zmianie uprawnień: ' + err.error.message);
        this.loading = false;
      }
    });
  }
}

