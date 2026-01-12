import { Component } from '@angular/core';
import { UserService } from '../../service/user.service';
import { ActivatedRoute } from '@angular/router';
import { User } from '../../model/user';
import { FilterSortSearchService } from '../../service/filter-sort-search.service';

@Component({
  selector: 'app-artist-list',
  standalone: false,
  templateUrl: './artist-list.component.html',
  styleUrl: './artist-list.component.css'
})
export class ArtistListComponent {
  role!: 'tattoo_artist' | 'trainee';
  users: User[] = [];
  filteredUsers: User[] = [];
  searchText = '';
  filterCity = '';
  sortOption = 'name-asc';
  p = 1;

  constructor(
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private filterSortSearchService: FilterSortSearchService
  ) { }

  ngOnInit(): void {
    this.role = this.activatedRoute.snapshot.data['role'];
    this.loadUsers();
  }

  loadUsers(): void {
    this.userService.getUsersByRole(this.role).subscribe((data) => {
      this.users = data.map(user => ({
        ...user,
        featuredPictures: user.featuredPictures ?? [],
      }));
      this.filteredUsers = [...this.users];
    });
  }

  applyFilters() {
    this.filteredUsers = this.filterSortSearchService.applyFilterSort(
      this.users,
      this.searchText,
      this.filterCity,
      this.sortOption
    );

    this.p = 1;
  }
}
