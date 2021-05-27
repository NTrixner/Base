import {Component, OnInit} from '@angular/core';
import {RegistrationDto, UserService} from '../../../api';
import {Router} from '@angular/router';
import {FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators} from '@angular/forms';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.less']
})
export class RegisterComponent implements OnInit {

  registrationDto: RegistrationDto = new class implements RegistrationDto {
    email: '';
    password: '';
    username: '';
  };
  hideA = true;
  hideB = true;
  password = '';

  userForm = new FormGroup({
      username: new FormControl('', [Validators.required, Validators.minLength(3), this.usernameValidator()]),
      email: new FormControl('', [Validators.required, Validators.email, this.emailValidator()]),
      password: new FormControl('', [Validators.required, Validators.minLength(8)]),
      passwordMatch: new FormControl('', [Validators.required])
    },
    RegisterComponent.passwordCheckValidator());

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

  public usernameValidator(): ValidatorFn {
    return (group: FormControl): ValidationErrors => {
      //TODO Check username availability
      return;
    }
  }

  static passwordCheckValidator(): ValidatorFn {
    return (fg: FormGroup) => {
      const passwordA = fg.get('password');
      const passwordB = fg.get('passwordMatch');
      let errors = {};
      if (passwordA.value !== passwordB.value) {
        passwordB.setErrors({notEquivalent: true});
      } else {
        passwordB.setErrors(null);
      }
      return errors;
    }
  }

  emailValidator() {
    return (group: FormControl): ValidationErrors => {
      //TODO Check email availability
      return;
    }
  }
}
