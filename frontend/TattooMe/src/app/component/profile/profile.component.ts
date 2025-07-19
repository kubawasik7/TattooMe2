import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
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
   @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  selectedFile: File | null = null;
  previewUrl: string | null = null;
  defaultAvatar = '/pobrane.png';


  constructor(private route: ActivatedRoute,
    private userService: UserService, private profileService: ProfileService
  ){}


ngOnInit(): void {
    const userId = this.route.snapshot.paramMap.get('id')!;

    this.userService.getUserById(userId).subscribe(user => {
      this.user = user;

      if (user.profilePicture) {
        this.previewUrl = `data:image/png;base64,${user.profilePicture}`;
      } else {
        this.previewUrl = null;  
    });
  }
   onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;
    this.selectedFile = input.files[0];

    const reader = new FileReader();
    reader.onload = () => this.previewUrl = reader.result as string;
    reader.readAsDataURL(this.selectedFile);
  }

  upload(): void {
    if (!this.selectedFile) return;
    this.profileService.uploadAvatar(this.selectedFile)
      .subscribe({
        next: () => {
          alert('Zdjęcie zostało zapisane.');
          this.selectedFile = null;
        },
        error: err => console.error('Błąd uploadu', err)
      });
  }

}
