import {Injectable} from '@angular/core';
import {UserDto, UserService} from '../../../api';
import {HttpResponse} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {Observable, Subscription} from 'rxjs';
import {Router} from '@angular/router';
import {RightsConstants} from '../../constants/rights-constants';

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
    return this.api.loginUser({username, password}, 'response')
      .pipe(tap((response: HttpResponse<any>) => {
          const authorization = response.headers.get('Authorization');
          if (response.ok && !!authorization) {
            const token = authorization.replace('Bearer ', '');
            this.setTokenAndLoadUserData(token).add(() => {
              this.router.navigateByUrl(returnUrl);
            });
          } else {
            this.router.navigateByUrl('login');
          }
        })
      );
  }

  public getCurrentUser(): UserDto | null {
    let userJson = localStorage.getItem('user');
    if (!!userJson) {
      let parsed = JSON.parse(userJson) as UserDto;
      if (!!parsed) {
        return parsed;
      }
    }
    return null;
  }

  public setTokenAndLoadUserData(token: string): Subscription {
    this.api.configuration.credentials['auth'] = 'Bearer ' + token;
    localStorage.setItem('userToken', token);
    return this.api.getCurrentUser('body').subscribe({
      next: (userDto: UserDto) => {
        if (userDto) {
          console.log("setting user to " + userDto.username);
          this.user = userDto;
          localStorage.setItem('user', JSON.stringify(userDto));
        }
      },
      error: () => {
        this.router.navigate(['login'], {queryParams: {message: 'You have been logged out. Please try again'}});
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
          localStorage.removeItem('userToken');
          localStorage.removeItem('user');
          this.router.navigateByUrl('/login');
        })
      )
      .subscribe();
  }

  public hasRight(right: string): boolean {
    return !!this.getCurrentUser()?.rights?.includes(right).valueOf() || false;
  }

  public canEdit(userId: string): boolean {
    return this.hasRight(RightsConstants.ROLE_USER_CAN_CREATE_USER)
      || this.isCurrentUser(userId) && this.hasRight(RightsConstants.ROLE_USER_CAN_SEE_SELF);
  }

  public canView(userId: string): boolean {
    return this.hasRight(RightsConstants.ROLE_USER_CAN_GET_USER_BY_ID)
      || this.isCurrentUser(userId) && this.hasRight(RightsConstants.ROLE_USER_CAN_SEE_SELF)
  }

  public canChangePassword(userId: string): boolean {
    return this.hasRight(RightsConstants.ROLE_USER_CAN_CHANGE_PASSWORD)
      || this.isCurrentUser(userId) && this.hasRight(RightsConstants.ROLE_USER_CAN_SEE_SELF)
  }

  public isCurrentUser(userId: string): boolean {
    return (!!this.getCurrentUser()?.id || '') == userId;
  }

  public showRightsError(): void {
    this.router.navigate(['error'], {queryParams: {message: 'rightError'}})
  }
}
