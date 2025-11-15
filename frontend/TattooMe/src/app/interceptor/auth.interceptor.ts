import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, catchError, throwError } from 'rxjs';
import { NotificationService } from '../service/notification.service';
@Injectable()

export class AuthInterceptor implements HttpInterceptor {

  constructor(private router: Router, private notification: NotificationService) { }

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
      catchError((err: any) => {
        if (err instanceof HttpErrorResponse && err.status === 401) {
          localStorage.removeItem('token');
          this.notification.showError('Twoja sesja wygasła. Zaloguj się ponownie.');
          this.router.navigate(['/login']);
        }
        return throwError(() => err);
      })
    );
  }
}