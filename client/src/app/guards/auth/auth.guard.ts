import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree,} from '@angular/router';
import type {Observable} from 'rxjs';
import {AuthService} from '../../services/auth/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private readonly router: Router, private readonly authService: AuthService) {
  }

  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    if (this.authService.user) {
      return true;
    } else {
      const userToken = window.sessionStorage.getItem('userToken');
      if (userToken != null) {
        this.authService.setTokenAndLoadUserData(userToken);
        return true;
      } else {
        this.router.navigate(['/login'], {
          queryParams: {returnUrl: state.url},
        });
        return false;
      }
    }
  }
}
