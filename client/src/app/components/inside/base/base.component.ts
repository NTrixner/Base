import {Component, HostListener} from '@angular/core';
import {AuthService} from '../../../services/auth/auth.service';
import {RightsConstants} from '../../../constants/rights-constants';
import {EditViewMode} from '../../../enums/edit-view-mode';

@Component({
  selector: 'app-base',
  templateUrl: './base.component.html',
  styleUrls: ['./base.component.less']
})
export class BaseComponent {

  public RightsConstants = RightsConstants;
  public EditViewMode = EditViewMode;
  events: string[] = [];
  appropriateClass: string = '';

  logout(): void {
    this.authService.logout();
  }

  constructor(public authService: AuthService) {
    this.getScreenHeight();
  }

  @HostListener('window:resize', ['$event'])
  getScreenHeight() {
    //console.log(window.innerHeight);
    if (window.innerHeight <= 412) {
      this.appropriateClass = 'bottomRelative';
    } else {
      this.appropriateClass = 'bottomStick';
    }
  }
}
