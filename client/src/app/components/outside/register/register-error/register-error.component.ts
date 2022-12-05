import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'app-register-error',
  templateUrl: './register-error.component.html',
  styleUrls: ['./register-error.component.less'],
})
export class RegisterErrorComponent {
  constructor(public router: Router) {
  }
}
