import {Injectable} from '@angular/core';
import {UserDto, UserService} from '../../../api';
import type {HttpResponse} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import type {Observable, Subscription} from 'rxjs';
import {Router} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  public user: UserDto | null = null;

  constructor(private readonly api: UserService, private readonly router: Router) {
    // Empty
  }

  public login(
    username: string,
    password: string,
    returnUrl: string
  ): Observable<HttpResponse<unknown>> {
    return this.api.loginUser({username, password}, 'response').pipe(
      tap((response: HttpResponse<unknown>) => {
        const authorization = response.headers.get('Authorization');
        if (response.ok && authorization) {
          const token = authorization.replace('Bearer ', '');
          this.setTokenAndLoadUserData(token).add(() => {
            this.router.navigateByUrl(returnUrl);
          });
        }
      })
    );
  }

  public setTokenAndLoadUserData(token: string): Subscription {
    this.api.configuration.credentials['auth'] = token;
    window.sessionStorage.setItem('userToken', token);
    return this.api.getCurrentUser('body').subscribe((userDto: UserDto) => {
      if (userDto) {
        this.user = userDto;
      }
    });
  }

  public logout(): void {
    this.api
      .logoutUser('body')
      .pipe(
        tap(() => {
          this.api.configuration.credentials = {};
          this.user = null;
          window.sessionStorage.removeItem('userToken');
          this.router.navigateByUrl('/login');
        })
      )
      .subscribe();
  }
}
