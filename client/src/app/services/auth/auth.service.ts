import {Injectable} from '@angular/core';
import {DefaultService, UserDto} from '../../../api';
import {HttpEvent, HttpResponse} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  public user: UserDto;
  public token: string;

  constructor(private api: DefaultService) {
    this.login('user', 'user');
  }

  public login(username: string, password: string): Observable<HttpResponse<any>> {
    return this.api.loginUser({username, password}, 'response')
      .pipe(
        tap((data: HttpResponse<any>) => {
          console.log(data.headers.keys());
          if (data.ok && data.headers.get('Authorization')) {
            this.token = data.headers.get('Authorization');
          }
        })
      );
  }

  public logout(): void {
    this.api.logoutUser('body').subscribe((data: HttpEvent<any>) => {
      console.log(data);
    });
  }

}
