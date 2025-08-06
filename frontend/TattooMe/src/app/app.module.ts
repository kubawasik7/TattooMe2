import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UserListComponent } from './component/user-list/user-list.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './component/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from './component/register/register.component';
import { RouterModule, Routes } from '@angular/router';
import { ProfileComponent } from './component/profile/profile.component';
import { TattooArtistListComponent } from './component/tattoo-artist-list/tattoo-artist-list.component';
import { TraineeListComponent } from './component/trainee-list/trainee-list.component';
import { MainPageComponent } from './component/main-page/main-page.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { AuthInterceptor } from './interceptor/auth.interceptor';
import { ArtistDateComponent } from './component/artist-date/artist-date.component';
import { UserProfileComponent } from './component/user-profile/user-profile.component';
import { UserInfoComponent } from './component/user-info/user-info.component';
const appRoutes: Routes = [
  { path: '', component: MainPageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {path: 'trainee', component: TraineeListComponent},
  {path: 'tattooArtist', component: TattooArtistListComponent},
  {path: 'profile/:id', component: ProfileComponent},
  {path: 'userProfile/:id', component: UserProfileComponent},
  {path: 'me/:id', component: UserInfoComponent},
  { path: '**', redirectTo: '' }
];

@NgModule({
  declarations: [
    AppComponent,
    UserListComponent,
    LoginComponent,
    RegisterComponent,
    ProfileComponent,
    TattooArtistListComponent,
    TraineeListComponent,
    MainPageComponent,
    ArtistDateComponent,
    UserProfileComponent,
    UserInfoComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    NgxPaginationModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    provideClientHydration(withEventReplay()),
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
