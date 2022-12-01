import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-forgot-password-error-component',
  templateUrl: './forgot-password-error.component.html',
  styleUrls: ['./forgot-password-error.component.less']
})
export class ForgotPasswordErrorComponent implements OnInit {

  constructor(public router: Router) {
  }

  ngOnInit(): void {
  }

}
