import { HttpHandler, HttpInterceptor, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
@Injectable()
export class AuthInterceptor implements HttpInterceptor {
 intercept(req: HttpRequest<any>, next: HttpHandler) {
  let authReq = req;
  if (typeof window !== 'undefined' && window.localStorage) {
    const token = window.localStorage.getItem('token');
    console.log('[AuthInterceptor] token=', token);
    if (token) {
      authReq = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
      console.log('[AuthInterceptor] authReq.headers=', authReq.headers);
    }
  }
  return next.handle(authReq);
}
}