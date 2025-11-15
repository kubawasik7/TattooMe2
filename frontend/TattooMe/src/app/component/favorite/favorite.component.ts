import { Component, OnInit } from '@angular/core';
import { FavoriteService } from '../../service/favorite.service';
import { Router } from '@angular/router';
import { FavoriteArtist } from '../../model/favorite-artist';
import { NotificationService } from '../../service/notification.service';
import Swal from 'sweetalert2';


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
        console.log(err)
      },
    });
  }

  removeFavorite(artistId: string): void {
    Swal.fire({
      title: 'Usunąć artystę z ulubionych?',
      text: 'Tej akcji nie można cofnąć.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#d33',
      cancelButtonColor: '#3085d6',
      background: '#1e1e1e',
      color: '#ffffff',
      confirmButtonText: 'Tak, usuń',
      cancelButtonText: 'Anuluj'
    }).then((result) => {
      if (result.isConfirmed) {
        this.favoriteService.removeFavorite(artistId).subscribe({
          next: () => {
            this.loadFavorites();
            this.notification.showSuccess("Artysta został usunięty z ulubionych");
          },
          error: (err) => {
            this.notification.showError("Nie udało się usunąć artysty z ulubionych");
          }
        });
      }
    });
  }

  goToArtistProfile(artistId: string): void {
    this.router.navigate(['/profile', artistId]);
  }
}