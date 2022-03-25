import type {OnInit} from '@angular/core';
import {Component} from '@angular/core';
import {AuthService} from '../../services/auth/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {catchError} from 'rxjs/operators';
import {of} from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less'],
})
export class LoginComponent implements OnInit {
  hide = true;
  password = '';
  username = '';
  returnUrl = '';
  loginFailureText = '';
  confirmationRegistration = false;

  constructor(
    private readonly authService: AuthService,
    private readonly route: ActivatedRoute,
    private readonly router: Router
  ) {
  }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
    this.confirmationRegistration =
      this.route.snapshot.queryParams['confirmationRegistration'] || false;
  }

  canLogin(): boolean {
    return !!(this.password && this.username);
  }

  login() {
    if (this.canLogin()) {
      this.authService
        .login(this.username, this.password, this.returnUrl)
        .pipe(
          catchError((err) => {
            this.loginFailureText = 'Login failed, please check the inputs';
            return of(err);
          })
        )
        .subscribe();
    }
  }

  register() {
    this.router.navigateByUrl('register');
  }
}
