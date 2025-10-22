import { Component, OnInit } from '@angular/core';
import { UserService } from '../../service/user.service';
import { User } from '../../model/user';

@Component({
  selector: 'app-main-page',
  standalone: false,
  templateUrl: './main-page.component.html',
  styleUrl: './main-page.component.css'
})

export class MainPageComponent implements OnInit {
  users: User[] = [];

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsersByRoleAVG().subscribe({
      next: data => {
        this.users = data.map((user: any) => ({
          ...user,
          profilePicture: user.profilePicture
            ? 'data:image/jpeg;base64,' + user.profilePicture
            : '/pobrane.png'
        }));
      }
    });
  }
}