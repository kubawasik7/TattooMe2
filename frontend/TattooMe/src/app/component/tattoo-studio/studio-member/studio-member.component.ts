import { Component } from '@angular/core';
import { User } from '../../../model/user';
import { StudioService } from '../../../service/studio.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-studio-member',
  standalone: false,
  templateUrl: './studio-member.component.html',
  styleUrl: './studio-member.component.css'
})
export class StudioMemberComponent {
   users: User[] = [];
  newUserId: string = '';
  studioId: string = '';
  newUserNickname: string = '';

  constructor(private studioService: StudioService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.studioId = this.route.snapshot.paramMap.get('id')!;
    this.loadUsers();
  }


  loadUsers(): void {
    this.studioService.getUsersByStudioId(this.studioId).subscribe({
      next: (data) => {
        this.users = data.map(user => ({
          ...user,
          featuredPictures: user.featuredPictures ?? []
        }));
          console.log(this.users);
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
}
