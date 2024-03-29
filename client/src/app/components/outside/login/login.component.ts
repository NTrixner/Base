import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../../services/auth/auth.service';
import {ActivatedRoute} from '@angular/router';
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
  message = null;
  loginFailureText = false;
  confirmationRegistration = false;

  constructor(
    private authService: AuthService,
    private route: ActivatedRoute
  ) {
  }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || 'base/home';
    this.message = this.route.snapshot.queryParams['message'] || null;
    this.confirmationRegistration =
      this.route.snapshot.queryParams['confirmationRegistration'] === 'true' || false;
  }

  canLogin(): boolean {
    return !!(this.password && this.username);
  }

  login() {
    if (this.canLogin()) {
      this.authService
        .login(this.username, this.password, this.returnUrl)
        .pipe(
          catchError(() => {
            this.loginFailureText = true;
            return of(null);
          })
        )
        .subscribe();
    }
  }
}
