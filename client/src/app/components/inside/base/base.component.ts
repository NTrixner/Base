import {Component} from '@angular/core';
import {AuthService} from '../../../services/auth/auth.service';
import {RightsConstants} from '../../../constants/rights-constants';

@Component({
  selector: 'app-base',
  templateUrl: './base.component.html',
  styleUrls: ['./base.component.less']
})
export class BaseComponent {

  public RightsConstants = RightsConstants;
  constructor(public authService: AuthService) {
  }

  logout(): void {
    this.authService.logout();
  }
}
