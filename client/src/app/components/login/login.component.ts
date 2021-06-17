import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth/auth.service';
import {ActivatedRoute, Router} from '@angular/router';
import {catchError} from 'rxjs/operators';
import {of} from 'rxjs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.less']
})
export class LoginComponent implements OnInit {
  hide: boolean = true;
  password: string = '';
  username: string = '';
  returnUrl: string = '';
  loginFailureText: string = '';

  constructor(private authService: AuthService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/home';
  }

  canLogin(): boolean {
    return !!(this.password && this.username);
  }

  login() {
    if (this.canLogin()) {
      this.authService.login(this.username, this.password, this.returnUrl)
        .pipe(
          catchError(err => {
            this.loginFailureText = 'Login failed, please check the inputs';
            return of(null);
          })
        )
        .subscribe();
    }
  }

  register() {
    this.router.navigateByUrl('register');
  }
}
