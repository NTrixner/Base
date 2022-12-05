import { Injectable } from '@angular/core';
import { UserDto, UserService } from '../../../api';
import { HttpResponse } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { Observable, Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  public user: UserDto | null = null;

  constructor(private api: UserService, private router: Router) {
    // Empty
  }

  public login(
    username: string,
    password: string,
    returnUrl: string
  ): Observable<HttpResponse<any>> {
    return this.api.loginUser({ username, password }, 'response').pipe(
      tap((response: HttpResponse<any>) => {
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

  public hasRight(right: string): boolean {
    return !!this.user?.rights?.includes(right).valueOf() || false;
  }
}
