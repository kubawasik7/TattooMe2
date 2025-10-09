import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserService } from '../../service/user.service';
import { User } from '../../model/user';

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

  ngOnInit(){
     const userId = this.route.snapshot.paramMap.get('id')!;

    this.userService.getUserById(userId).subscribe(user => {
      this.user = {...user};
      this.originalUser = {...user};
    });
  }
    saveChanges(): void {
    this.userService.updateUser(this.user).subscribe(() => {
      this.originalUser = { ...this.user };
      this.editMode = false;
    });
  }
   cancelChanges(): void {
    this.user = { ...this.originalUser };
    this.editMode = false;
  }


}
