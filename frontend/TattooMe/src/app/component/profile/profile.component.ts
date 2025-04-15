import { Component, OnInit } from '@angular/core';
import { User, UserService } from '../../service/user.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  user!: User;
  userId!: string;

  constructor(private route: ActivatedRoute,
    private userService: UserService
  ){}

  ngOnInit(): void {
    this.userId = this.route.snapshot.paramMap.get('id')!;
    if(this.userId){
      this.userService.getUserById(this.userId).subscribe(data =>{
        this.user = data;
      })
    }
  }

}
