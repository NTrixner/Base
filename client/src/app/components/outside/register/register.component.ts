import {Component} from '@angular/core';
import {RegistrationDto, UserService} from '../../../../api';
import {Router} from '@angular/router';
import {AsyncValidatorFn, FormControl, FormGroup, ValidationErrors, Validators,} from '@angular/forms';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs';
import {UserUtils} from '../../../services/utils/user-utils';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.less'],
})
export class RegisterComponent {
  registrationDto: RegistrationDto = {
    email: '',
    password: '',
    username: '',
  };
  hideA = true;
  hideB = true;
  password = '';

  userForm = new FormGroup(
    {
      username: new FormControl(
        '',
        [Validators.required, Validators.minLength(3)],
        [this.usernameValidator()]
      ),
      email: new FormControl(
        '',
        [Validators.required, Validators.email],
        [this.emailValidator()]
      ),
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
      ]),
      passwordMatch: new FormControl('', [Validators.required]),
    },
    UserUtils.passwordCheckValidator()
  );

  constructor(private userService: UserService, public router: Router) {
  }

  register() {
    let registrationDto: RegistrationDto = {
      username: this.userForm.get('username')?.value || '',
      password: this.userForm.get('password')?.value || '',
      email: this.userForm.get('email')?.value || '',
    };
    this.userService
      .registerUser(registrationDto, 'response')
      .subscribe((value) => {
        if (value.status === 200) {
          this.router.navigateByUrl('register/success');
        } else {
          this.router.navigateByUrl('register/error');
        }
      });
  }

  private emailValidator(): AsyncValidatorFn {
    return (fc: FormControl): Observable<ValidationErrors | null> => {
      return this.userService.isEmailAvailable(fc.value, [''], 'body')
        .pipe(map((isAvailable) => (!isAvailable ? {used: true} : null)));
    };
  }

  private usernameValidator(): AsyncValidatorFn {
    return (fc: FormControl): Observable<ValidationErrors | null> => {
      return this.userService.isUsernameAvailable(fc.value, [''], 'body')
        .pipe(map((isAvailable) => (!isAvailable ? {used: true} : null)));
    };
  }

}
