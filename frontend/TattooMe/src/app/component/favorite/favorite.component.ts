import { Component, OnInit } from '@angular/core';
import { FavoriteService } from '../../service/favorite.service';
import { Router } from '@angular/router';
import { FavoriteArtist } from '../../model/favorite-artist';
import { NotificationService } from '../../service/notification.service';
import Swal from 'sweetalert2';
import { User } from '../../model/user';


@Component({
  selector: 'app-favorite',
  standalone: false,
  templateUrl: './favorite.component.html',
  styleUrl: './favorite.component.css'
})
export class FavoriteComponent implements OnInit {
  users: User[] = [];
  p = 1;

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
        this.users = data.map(u => ({
          ...u,
          featuredPictures: u.featuredPictures ?? []
        }));
      },
      error: () => {
        this.notification.showError('Nie udało się pobrać ulubionych');
      }
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
            this.users = this.users.filter(u => u.id !== artistId);
            this.notification.showSuccess('Artysta został usunięty z ulubionych');
          },
          error: () => {
            this.notification.showError('Nie udało się usunąć artysty z ulubionych');
          }
        });
      }
    });
  }

  goToArtistProfile(artistId: string): void {
    this.router.navigate(['/profile', artistId]);
  }
}