import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration, withEventReplay } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './component/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RegisterComponent } from './component/register/register.component';
import { RouterModule, Routes } from '@angular/router';
import { ProfileComponent } from './component/profile/profile.component';
import { MainPageComponent } from './component/main-page/main-page.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { AuthInterceptor } from './interceptor/auth.interceptor';
import { ArtistDateComponent } from './component/profile/artist-date/artist-date.component';
import { UserProfileComponent } from './component/user-profile/user-profile.component';
import { UserInfoComponent } from './component/user-info/user-info.component';
import { FavoriteComponent } from './component/favorite/favorite.component';
import { ContactComponent } from './component/contact/contact.component';
import { StyleComponent } from './component/profile/style/style.component';
import { OfferComponent } from './component/profile/offer/offer.component';
import { SpecialOfferComponent } from './component/profile/special-offer/special-offer.component';
import { PortfolioComponent } from './component/profile/portfolio/portfolio.component';
import { ArtistListComponent } from './component/artist-list/artist-list.component';
import { ChatListComponent } from './component/chat-list/chat-list.component';
import { ChatWindowComponent } from './component/chat-window/chat-window.component';
import { VisitComponent } from './component/profile/visit/visit.component';
import { CreateStudioComponent } from './component/create-studio/create-studio.component';
const appRoutes: Routes = [
  { path: '', component: MainPageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {path: 'trainee', component: ArtistListComponent, data: {role: 'trainee'}},
  {path: 'tattooArtist', component: ArtistListComponent, data: {role: 'tattoo_artist'}},
  {path: 'profile/:id', component: ProfileComponent},
  {path: 'userProfile/:id', component: UserProfileComponent},
  {path: 'me/:id', component: UserInfoComponent},
  {path: 'favorites/:id', component: FavoriteComponent},
  {path: 'contact', component: ContactComponent},
  { path: 'chat/:id', component: ChatWindowComponent },
  { path: 'chats', component: ChatListComponent },
  { path: '**', redirectTo: '' }
];

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegisterComponent,
    ProfileComponent,
    MainPageComponent,
    ArtistDateComponent,
    UserProfileComponent,
    UserInfoComponent,
    FavoriteComponent,
    ContactComponent,
    StyleComponent,
    OfferComponent,
    SpecialOfferComponent,
    PortfolioComponent,
    ArtistListComponent,
    ChatListComponent,
    ChatWindowComponent,
    VisitComponent,
    CreateStudioComponent
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
