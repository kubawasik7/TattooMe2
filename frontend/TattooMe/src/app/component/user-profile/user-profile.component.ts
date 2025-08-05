import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User, UserService } from '../../service/user.service';

@Component({
  selector: 'app-user-profile',
  standalone: false,
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent {
  user!: User;
  userId!: string;
  originalUser!: User;
  editMode = false;

  constructor(private route: ActivatedRoute, private userService: UserService){}


}
