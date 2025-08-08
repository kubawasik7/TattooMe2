import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { User, UserService } from '../../service/user.service';
import { ActivatedRoute } from '@angular/router';
import { CreateOffer, Offer, ProfileService } from '../../service/profile.service';
import { TattooStyle } from '../../model/tattoo-style';
import { WorkStyleService } from '../../service/work-style.service';
import { FavoriteService } from '../../service/favorite.service';
import { DomSanitizer, SafeUrl } from '@angular/platform-browser';
import { PortfolioService } from '../../service/portfolio.service';
import { Portfolio } from '../../model/portfolio';
import { Flash } from '../../model/flash';

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
  originalSelectedStyleIds: string[] = [];
  portfolioItems: Portfolio[] = [];
  showAllPortfolio = false;
  flashes: Flash[] = [];
  showFlashModal = false;
  flashFile: File | null = null;
  newFlash: Flash = {
    description: '',
    reccomendedPlace: '',
    sizeMin: 0,
    sizeMax: 0,
    priceMin: 0,
    priceMax: 0
  };


  draft: CreateOffer = { startDate: '', endDate: '', description: '' };
   @ViewChild('fileInput') fileInput!: ElementRef<HTMLInputElement>;
  selectedFile: File | null = null;
  previewUrl: string | null = null;
  defaultAvatar = '/pobrane.png';


  constructor(private route: ActivatedRoute,
    private userService: UserService, private profileService: ProfileService,
    private workStyleService: WorkStyleService, private favoriteService: FavoriteService,
    private portfolioService: PortfolioService
  ){}


ngOnInit(): void {
    this.userId = this.route.snapshot.paramMap.get('id')!;

    this.workStyleService.getAllStyles().subscribe(styles => this.allStyles = styles);

    this.workStyleService.getUserStyles(this.userId).subscribe(styles => {
      this.styles = styles;
      this.selectedStyleIds = styles.map(s => s.id);
    });

      this.portfolioService.getByUser(this.userId).subscribe(items => {
      this.portfolioItems = items;
    });


    this.userService.getUserById(this.userId).subscribe(user => {
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
  //SEKCJA FAVORITE
  addToFavorites(artistId: string) {
  this.favoriteService.addFavorite(artistId).subscribe(() => {
    alert('Dodano do ulubionych');
  });
}


  //SEKCJA STYLE
  startEditStyle(): void {
  this.editStyleMode = true;
  this.originalSelectedStyleIds = [...this.selectedStyleIds];
  }

  cancelEditStyle(): void {
  this.selectedStyleIds = [...this.originalSelectedStyleIds];
  this.editStyleMode = false;
  }

  saveStyles(): void {
  this.workStyleService.saveUserStyles(this.userId, this.selectedStyleIds)
    .subscribe(() => {
      this.editStyleMode = false;
      alert('Zapisano style.');
    });
  }

  toggleStyle(id: string): void {
    if (this.selectedStyleIds.includes(id)) {
      this.selectedStyleIds = this.selectedStyleIds.filter(s => s !== id);
    } else {
      this.selectedStyleIds.push(id);
    }
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

  //SEKCJA PORTFOLIO
  deleteImagePortfolio(id: string) {
  this.portfolioService.delete(id).subscribe(() => {
    this.portfolioItems = this.portfolioItems.filter(p => p.id !== id);
  });

}

  onFileSelectedPortfolio(event: any): void {
    if (event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  uploadPortfolio(): void {
    if (!this.selectedFile) return;

    this.portfolioService.uploadImage(this.selectedFile).subscribe({
      next: () => {
        this.selectedFile = null;
      },
      error: err => console.error('Błąd uploadu portfolio', err)
    });
  }
}
