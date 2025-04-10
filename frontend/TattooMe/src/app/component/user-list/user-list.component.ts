import { Component, OnInit } from '@angular/core';
import { UserService, User } from '../../service/user.service';


@Component({
  selector: 'app-user-list',
  standalone: false,
  templateUrl: './user-list.component.html',
  styleUrl: './user-list.component.css'
})
export class UserListComponent implements OnInit{
  users: User[] = [];
  constructor(private userService: UserService){}

  ngOnInit(): void {
    this.loadUsers();
  }
  loadUsers(): void {
    this.userService.getUsers().subscribe(data =>{
      this.users = data;
    });
  }

}
