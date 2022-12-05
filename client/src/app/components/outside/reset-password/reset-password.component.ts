import {Component, OnInit} from '@angular/core';
import {PasswordResetDto, UserService} from '../../../../api';
import {FormControl, FormGroup, ValidatorFn, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';

@Component({
  selector: 'app-rest-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.less']
})
export class ResetPasswordComponent implements OnInit {

  forgotPasswordDto: PasswordResetDto = {
    uuid: '',
    newPassword: '',
  };
  hideA = true;
  hideB = true;
  password = '';
  error = false;

  passwordForm = new FormGroup(
    {
      password: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
      ]),
      passwordMatch: new FormControl('', [Validators.required]),
    },
    this.passwordCheckValidator()
  );

  constructor(private userService: UserService, public router: Router, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.forgotPasswordDto.uuid =
      this.route.snapshot.queryParams['uuid'];
    if (!this.forgotPasswordDto.uuid) {
      this.error = true;
    }
  }

  submit() {
    let resetDto: PasswordResetDto = {
      newPassword: this.passwordForm.get('password')?.value || '',
      uuid: this.forgotPasswordDto.uuid
    };
    this.userService
      .resetPasswordRequest(resetDto, 'response')
      .subscribe((value) => {
        if (value.status === 200) {
          this.router.navigateByUrl('resetPassword/success');
        } else {
          this.router.navigateByUrl('resetPassword/error');
        }
      });
  }

  passwordCheckValidator(): ValidatorFn {
    return (fg: FormGroup) => {
      const passwordA = fg.get('password');
      const passwordB = fg.get('passwordMatch');
      if (!!passwordA && !!passwordB) {
        if (passwordA.value !== passwordB.value) {
          passwordB.setErrors({...passwordB.errors, notEquivalent: true});
        } else if (!!passwordB.errors) {
          passwordB.setErrors({...passwordB.errors});
        } else {
          passwordB.setErrors(null);
        }
        return passwordB.errors;
      } else {
        return null;
      }
    };
  }
}
