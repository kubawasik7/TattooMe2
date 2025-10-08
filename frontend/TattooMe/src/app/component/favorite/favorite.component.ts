import { Component, OnInit } from '@angular/core';
import { FavoriteService } from '../../service/favorite.service';
import { User } from '../../service/user.service';
import { Router } from '@angular/router';
import { FavoriteArtist } from '../../model/favorite-artist';


@Component({
  selector: 'app-favorite',
  standalone: false,
  templateUrl: './favorite.component.html',
  styleUrl: './favorite.component.css'
})
export class FavoriteComponent implements OnInit{
favorites: FavoriteArtist[] = [];

  constructor(private favoriteService: FavoriteService, private router: Router) {}

  ngOnInit(): void {
    this.loadFavorites();
  }

  loadFavorites(): void {
    this.favoriteService.getFavorites().subscribe(data => {
      this.favorites = data;
    });
  }

  removeFavorite(artistId: string): void {
    this.favoriteService.removeFavorite(artistId).subscribe(() => {
      this.loadFavorites();
    });
  }
  goToArtistProfile(artistId: string): void {
  this.router.navigate(['/profile', artistId]);
}
}