import {Injectable} from '@angular/core';
import {DefaultService, UserDto} from '../../../api';
import {HttpEvent, HttpResponse} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {Observable} from 'rxjs';
import { createOfflineCompileUrlResolver } from '@angular/compiler';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  public user: UserDto;

  constructor(private api: DefaultService, private router: Router) {
    // Empty
  }

  public login(username: string, password: string, returnUrl: string): Observable<HttpResponse<any>> {
    return this.api.loginUser({username, password}, 'response')
      .pipe(
        tap((response: HttpResponse<any>) => {
          if (response.ok && response.headers.get('Authorization')) {
            this.api.configuration.accessToken = response.headers.get('Authorization').replace('Bearer ', '');
            this.api.getCurrentUser('body').subscribe((userDto: UserDto) => {
              if (userDto) {
                this.user = userDto;
                this.router.navigateByUrl(returnUrl);
              }
            });
          }
        })
      );
  }

  public logout(): void {
    this.api.logoutUser('body').pipe(
      tap((data: HttpResponse<any>) => {
        this.api.configuration.accessToken = null;
        this.user = null;
        this.router.navigateByUrl('/login');
      })
    ).subscribe();
  }
}
