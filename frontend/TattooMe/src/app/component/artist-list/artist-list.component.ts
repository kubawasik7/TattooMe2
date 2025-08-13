import { Component } from '@angular/core';
import { User, UserService } from '../../service/user.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-artist-list',
  standalone: false,
  templateUrl: './artist-list.component.html',
  styleUrl: './artist-list.component.css'
})
export class ArtistListComponent {
  role!: 'tattoo_artist' | 'trainee';
  users: User[] = [];
  p = 1;

  constructor(private userService: UserService, private activatedRoute: ActivatedRoute) { }
  ngOnInit(): void {
    this.role = this.activatedRoute.snapshot.data['role'];
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsersByRole(this.role).subscribe({
      next: data => { this.users = data; }
    })
  }
}
