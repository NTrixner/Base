import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {AuthService} from '../services/auth/auth.service';
import {Router} from '@angular/router';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private service: AuthService, private router: Router) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(tap(event => {
      let response = event as HttpResponse<any>;
      if (!!response && response.ok) {
        const authorization = response.headers.get('Authorization');
        if (!!authorization) {
          const token = authorization.replace('Bearer ', '');
          let oldToken = localStorage.getItem('token');
          if (token != oldToken) {
            this.service.setTokenAndLoadUserData(token);
          }
        }
      } else if (!response.ok) {
        if (response.status >= 400 && response.status <= 499) {
          this.router.navigate(['/login'], {queryParams: {message: 'You have been logged out. Please try again'}});
        }
      }
    }));
  }
}
