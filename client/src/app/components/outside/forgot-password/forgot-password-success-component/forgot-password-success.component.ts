import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-forgot-password-success-component',
  templateUrl: './forgot-password-success.component.html',
  styleUrls: ['./forgot-password-success.component.less']
})
export class ForgotPasswordSuccessComponent {

  constructor(public router: Router) {
  }

}
