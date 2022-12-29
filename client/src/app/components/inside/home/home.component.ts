import {Component} from '@angular/core';
import {AuthService} from '../../../services/auth/auth.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.less'],
})
export class HomeComponent {

  constructor(
    private authService: AuthService
  ) {
  }

  getUsername(): string {
    return this.authService.user?.username || 'Anonymous';
  }

  logout(): void {
    this.authService.logout();
  }

}
