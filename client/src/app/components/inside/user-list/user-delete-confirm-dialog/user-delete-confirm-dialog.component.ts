import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {UserDto, UserService} from '../../../../../api';
import {DialogResult, DialogResults} from '../../../../interfaces/dialog-result';

@Component({
  selector: 'app-user-delete-confirm-dialog',
  templateUrl: './user-delete-confirm-dialog.component.html',
  styleUrls: ['./user-delete-confirm-dialog.component.less']
})
export class UserDeleteConfirmDialogComponent {
  constructor(public dialogRef: MatDialogRef<UserDeleteConfirmDialogComponent, DialogResult>,
              @Inject(MAT_DIALOG_DATA) public user: UserDto,
              public userService: UserService,) {
  }

  onNoClick() {
    this.dialogRef.close(DialogResults.CANCEL);
  }

  onYesClick() {
    let userId = this.user.id!;
    this.userService.deleteUser(userId)
      .subscribe({
        next: () => this.dialogRef.close(DialogResults.OK),
        error: (error) => this.dialogRef.close(DialogResults.error(error))
      });
  }
}
