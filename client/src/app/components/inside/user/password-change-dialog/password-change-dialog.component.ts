import {Component, Inject} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {UserUtils} from '../../../../services/utils/user-utils';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ChangePasswordDto, UserDto, UserService} from '../../../../../api';
import {AuthService} from '../../../../services/auth/auth.service';
import {DialogResult, DialogResults} from '../../../../interfaces/dialog-result';

@Component({
  selector: 'app-password-change-dialog',
  templateUrl: './password-change-dialog.component.html',
  styleUrls: ['./password-change-dialog.component.less']
})
export class PasswordChangeDialogComponent {
  public hideA: boolean = true;
  public hideB: boolean = true;

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

  constructor(public dialogRef: MatDialogRef<PasswordChangeDialogComponent, DialogResult>,
              @Inject(MAT_DIALOG_DATA) public user: UserDto,
              public userService: UserService,
              public auth: AuthService) {
  }

  submit() {
    let changeDto: ChangePasswordDto = {
      userId: this.user?.id ?? '',
      newPassword: this.passwordForm.get('password')?.value || ''
    }
    this.userService
      .changePassword(changeDto, 'response')
      .subscribe({
        next: () => this.dialogRef.close(DialogResults.OK),
        error: error => this.dialogRef.close(DialogResults.error(error))
      });
  }

  cancel() {
    this.dialogRef.close(DialogResults.CANCEL);
  }
}
