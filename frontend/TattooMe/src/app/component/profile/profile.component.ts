import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { User, UserService } from '../../service/user.service';
import { ActivatedRoute } from '@angular/router';
import { CreateOffer, Offer, ProfileService } from '../../service/profile.service';
import { TattooStyle } from '../../model/tattoo-style';
import { WorkStyleService } from '../../service/work-style.service';

@Component({
  selector: 'app-profile',
  standalone: false,
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  user!: User;
  userId!: string;
  editing = false;
  description: string = '';
  draftDescription: string = '';
  offers: Offer[] = [];
  editingId: string | null = null;
  styles: TattooStyle[] = [];
  allStyles: TattooStyle[] = [];
  editStyleMode = false;
  selectedStyleIds: string[] = [];

  draft: CreateOffer = { startDate: '', endDate: '', description: '' };
   @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  selectedFile: File | null = null;
  previewUrl: string | null = null;
  defaultAvatar = '/pobrane.png';


  constructor(private route: ActivatedRoute,
    private userService: UserService, private profileService: ProfileService,
    private workStyleService: WorkStyleService
  ){}


ngOnInit(): void {
    const userId = this.route.snapshot.paramMap.get('id')!;

    this.userService.getUserById(userId).subscribe(user => {
      this.user = user;

      if (user.profilePicture) {
        this.previewUrl = `data:image/png;base64,${user.profilePicture}`;
      } else {
        this.previewUrl = null;  
      }
      this.load();
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
  startEdit(): void {
    this.draftDescription = this.description;
    this.editing = true;
  }

  cancelEdit(): void {
    this.editing = false;
    this.draftDescription = '';
  }

  saveDescription(): void {
    this.profileService.updateDescription(this.draftDescription)
      .subscribe({
        next: updated => {
          this.description = updated.description;
          this.editing = false;
        },
        error: err => console.error('Błąd zapisu opisu', err)
      });
  }

  //SEKCJA OFERTY
   load() {
    this.profileService.getOffers().subscribe(list => this.offers = list);
  }
  startNew() {
    this.editingId = 'new';
    this.draft = { startDate: '', endDate: '', description: '' };
  }

  startEditOffer(o: Offer) {
    this.editingId = o.id;
    this.draft = {
      startDate: o.startDate,
      endDate: o.endDate,
      description: o.description
    };
  }

  save() {
    if (this.editingId === 'new') {
      this.profileService.createOffer(this.draft).subscribe(() => this.load());
    } else {
      this.profileService.updateOffer(this.editingId!, this.draft).subscribe(() => this.load());
    }
    this.editingId = null;
  }

  cancel() {
    this.editingId = null;
  }

  delete(id: string) {
    if (confirm('Usunąć tę ofertę?')) {
      this.profileService.deleteOffer(id).subscribe(() => this.load());
    }
  }
}
