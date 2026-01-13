import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError, throwError } from 'rxjs';
import { NotificationService } from '../service/notification.service';
import { AuthService } from '../service/auth.service';
@Injectable()

export class AuthInterceptor implements HttpInterceptor {

  constructor(private router: Router,
    private notification: NotificationService,
    private authService: AuthService) { }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let authReq = req;

    if (typeof window !== 'undefined' && window.localStorage) {
      const token = window.localStorage.getItem('token');
      if (token) {
        authReq = req.clone({
          setHeaders: { Authorization: `Bearer ${token}` }
        });
      }
    }
    return next.handle(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          this.authService.logout();
          this.notification.showWarning('Sesja wygasła. Zostałeś wylogowany.');
          this.router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }
}