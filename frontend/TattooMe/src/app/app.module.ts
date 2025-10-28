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
import { VisitComponent } from './component/profile/visit/visit.component';
import { CreateStudioComponent } from './component/create-studio/create-studio.component';
import { StudioListComponent } from './component/studio-list/studio-list.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { ReservationsComponent } from './component/reservations/reservations.component';
import { ReviewsComponent } from './component/review/review.component';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { TattooStudioComponent } from './component/tattoo-studio/tattoo-studio.component';
import { StudioMemberComponent } from './component/tattoo-studio/studio-member/studio-member.component';
import { FaqComponent } from './component/tattoo-studio/faq/faq.component';
const appRoutes: Routes = [
  { path: '', component: MainPageComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'trainee', component: ArtistListComponent, data: { role: 'trainee' } },
  { path: 'tattooArtist', component: ArtistListComponent, data: { role: 'tattoo_artist' } },
  { path: 'profile/:id', component: ProfileComponent },
  { path: 'userProfile/:id', component: UserProfileComponent },
  { path: 'me/:id', component: UserInfoComponent },
  { path: 'favorites/:id', component: FavoriteComponent },
  { path: 'contact', component: ContactComponent },
  { path: 'chats', component: ChatListComponent },
  { path: 'createStudio', component: CreateStudioComponent },
  { path: 'studios', component: StudioListComponent },
  { path: 'studio/:id', component: TattooStudioComponent },
  { path: 'reservations', component: ReservationsComponent },
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
    VisitComponent,
    CreateStudioComponent,
    StudioListComponent,
    ReservationsComponent,
    ReviewsComponent,
    TattooStudioComponent,
    StudioMemberComponent,
    FaqComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    NgxPaginationModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(appRoutes),
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSnackBarModule,
    MatButtonModule,
    MatIconModule
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
