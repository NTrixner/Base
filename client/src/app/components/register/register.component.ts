import {Component, OnInit} from '@angular/core';
import {RegistrationDto, UserService} from '../../../api';
import {Router} from '@angular/router';
import {AsyncValidatorFn, FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.less']
})
export class RegisterComponent implements OnInit {

  registrationDto: RegistrationDto = {
    email: '',
    password: '',
    username: ''
  };
  hideA = true;
  hideB = true;
  password = '';

  userForm = new FormGroup({
      username: new FormControl('', [Validators.required, Validators.minLength(3)], [this.usernameValidator()]),
      email: new FormControl('', [Validators.required, Validators.email], [this.emailValidator()]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)]),
      passwordMatch: new FormControl('', [Validators.required])
    },
    this.passwordCheckValidator());

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {
  }

  register() {
    this.userService
      .registerUser({
        username: this.userForm.get('username').value,
        password: this.userForm.get('password').value,
        email: this.userForm.get('email').value
      })
      .subscribe();
  }

  goBackToLogin() {
    this.router.navigateByUrl('login');
  }


  passwordCheckValidator(): ValidatorFn {
    return (fg: FormGroup) => {
      const passwordA = fg.get('password');
      const passwordB = fg.get('passwordMatch');
      if (passwordA.value !== passwordB.value) {
        passwordB.setErrors({...passwordB.errors, notEquivalent: true});
      } else if (!!passwordB.errors) {
        passwordB.setErrors({...passwordB.errors});
      } else {
        passwordB.setErrors(null);
      }
      return passwordB.errors;
    };
  }

  emailValidator(): AsyncValidatorFn {
    return (fc: FormControl) => {
      if (!!fc.value) {
        return this.userService.isEmailAvailable(fc.value, 'body').pipe(
          map(isAvailable => !isAvailable ? {used: true} : null)
        );
      }
    };
  }

  usernameValidator(): AsyncValidatorFn {
    return (fc: FormControl) => {
      if (!!fc.value) {
        return this.userService.isUsernameAvailable(fc.value, 'body').pipe(
          map(isAvailable => !isAvailable ? {used: true} : null)
        );
      }
    };
  }
}
