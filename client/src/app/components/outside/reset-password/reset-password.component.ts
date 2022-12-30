import {Component, OnInit} from '@angular/core';
import {PasswordResetDto, UserService} from '../../../../api';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {UserUtils} from '../../../services/utils/user-utils';

@Component({
  selector: 'app-rest-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.less']
})
export class ResetPasswordComponent implements OnInit {

  forgotPasswordDto: PasswordResetDto = {
    uuid: '',
    newPassword: ''
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
    UserUtils.passwordCheckValidator()
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
}
