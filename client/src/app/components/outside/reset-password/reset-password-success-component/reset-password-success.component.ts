import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-forgot-password-success-component',
  templateUrl: './reset-password-success.component.html',
  styleUrls: ['./reset-password-success.component.less']
})
export class ResetPasswordSuccessComponent implements OnInit {

  constructor(public router: Router) {
  }

  ngOnInit(): void {
  }
}
