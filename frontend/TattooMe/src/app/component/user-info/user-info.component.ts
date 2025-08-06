import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-user-info',
  standalone: false,
  templateUrl: './user-info.component.html',
  styleUrl: './user-info.component.css'
})
export class UserInfoComponent implements OnInit {
  info: any = {};
  originalInfo: any = {};
  editMode = false;

  constructor(private infoService: UserInfoService) {}

  ngOnInit(): void {
    this.infoService.getInfo().subscribe(data => {
      console.log('Dane z backendu:', data);
      this.info = { ...data };
      console.log('info:', this.info);

      this.originalInfo = { ...data };
    });
  }

  enableEdit(): void {
    this.editMode = true;
  }

  saveChanges(): void {
    this.infoService.updateInfo(this.info).subscribe({
      next: () => {
        this.originalInfo = { ...this.info };
        this.editMode = false;
        alert('Zapisano informacje');
      },
      error: () => alert('Błąd')
    });
  }

  cancelChanges(): void {
    this.info = { ...this.originalInfo };
    this.editMode = false;
  }

}
