import {Injectable} from '@angular/core';
import {DefaultService, UserDto} from '../../../api';
import {HttpEvent, HttpResponse} from '@angular/common/http';
import {catchError} from 'rxjs/operators';
import {of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  public user: UserDto;
  public token: string;

  constructor(private api: DefaultService) {
    this.login('user', 'user');
  }

  public login(username: string, password: string): void {
    console.log(`Trying to log in ${username} with password ${password}`);
    this.api.loginUser({username, password}, 'response')
      .pipe(
        catchError(err => {
          console.log(err);
          return of([]);
        })
      )
      .subscribe((data: HttpResponse<any>) => {
        console.log(data.headers.keys());
        if (data.ok && data.headers.get('Authorization')) {
          this.token = data.headers.get('Authorization');
        }
      });
  }

  public logout(): void {
    this.api.logoutUser('body').subscribe((data: HttpEvent<any>) => {
      console.log(data);
    });
  }

}
