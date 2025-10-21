import { Component, OnInit } from '@angular/core';
import { FavoriteService } from '../../service/favorite.service';
import { Router } from '@angular/router';
import { FavoriteArtist } from '../../model/favorite-artist';
import { NotificationService } from '../../service/notification.service';


@Component({
  selector: 'app-favorite',
  standalone: false,
  templateUrl: './favorite.component.html',
  styleUrl: './favorite.component.css'
})
export class FavoriteComponent implements OnInit {
  favorites: FavoriteArtist[] = [];

  constructor(
    private favoriteService: FavoriteService,
    private router: Router,
    private notification: NotificationService
  ) { }

  ngOnInit(): void {
    this.loadFavorites();
  }

  loadFavorites(): void {
    this.favoriteService.getFavorites().subscribe({
      next: (data) => {
        this.favorites = data;
      },
      error: (err) => {
        this.notification.showError("Nie udało się załadować ulubionych artystów", err);
      },
    });
  }

  removeFavorite(artistId: string): void {
    this.favoriteService.removeFavorite(artistId).subscribe({
      next: () => {
        this.loadFavorites();
        this.notification.showSuccess("Artysta został usunięty z ulubionych");
      },
      error: (err) => {
        this.notification.showError("Nie udało się usunąć artysty z ulubionych", err);
      },
    });
  }

  goToArtistProfile(artistId: string): void {
    this.router.navigate(['/profile', artistId]);
  }
}