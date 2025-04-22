import { Component } from '@angular/core';
import { User, UserService } from '../../service/user.service';

@Component({
  selector: 'app-tattoo-artist-list',
  standalone: false,
  templateUrl: './tattoo-artist-list.component.html',
  styleUrl: './tattoo-artist-list.component.css'
})
export class TattooArtistListComponent {
  users: User[] = [];
  p = 1;

  constructor(private userService: UserService){}
  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getTattooArtists().subscribe(data => {
      this.users = data;
    });
  }
}
