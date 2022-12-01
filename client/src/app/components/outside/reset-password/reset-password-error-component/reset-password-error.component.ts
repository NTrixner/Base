import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-forgot-password-error-component',
  templateUrl: './reset-password-error.component.html',
  styleUrls: ['./reset-password-error.component.less']
})
export class ResetPasswordErrorComponent {

  constructor(public router: Router) {
  }

}
