import { Component } from '@angular/core';
import { User, UserService } from '../../service/user.service';

@Component({
  selector: 'app-trainee-list',
  standalone: false,
  templateUrl: './trainee-list.component.html',
  styleUrl: './trainee-list.component.css'
})
export class TraineeListComponent {
  users: User[] = [];
  p = 1;

  constructor(private userService: UserService){}
  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getTrainees().subscribe(data => {
      this.users = data;
    });
  }
}
