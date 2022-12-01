import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register-success',
  templateUrl: './register-success.component.html',
  styleUrls: ['./register-success.component.less'],
})
export class RegisterSuccessComponent {
  constructor(public router: Router) {
  }
}
