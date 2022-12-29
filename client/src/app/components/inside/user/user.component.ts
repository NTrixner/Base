import {Component, Input, OnInit} from '@angular/core';
import {EditViewMode} from '../../../enums/edit-view-mode';
import {UserDto, UserService} from '../../../../api';
import {ActivatedRoute} from '@angular/router';
import {AuthService} from '../../../services/auth/auth.service';
import {RightsConstants} from '../../../constants/rights-constants';
import {AsyncValidatorFn, FormControl, FormGroup, ValidationErrors, Validators} from '@angular/forms';
import {PasswordChangeDialogComponent} from './password-change-dialog/password-change-dialog.component';
import {MatDialog} from '@angular/material/dialog';
import {DialogResult} from '../../../interfaces/dialog-result';
import {DialogResultStatus} from '../../../enums/dialog-result-status';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.less']
})
export class UserComponent implements OnInit {
  public EditViewMode = EditViewMode;

  @Input() public mode: EditViewMode = EditViewMode.VIEW;
  public canEdit: boolean = false;
  public canChangePassword: boolean = false;
  public user: UserDto = {};

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
    }
  );

  constructor(private service: UserService,
              private auth: AuthService,
              private route: ActivatedRoute,
              public dialog: MatDialog) {
  }

  ngOnInit(): void {
    let userId: string = this.route.snapshot.queryParams['user'] || null;
    if (!userId) {
      //Create mode, check if we can do this
      if (!this.auth.hasRight(RightsConstants.ROLE_USER_CAN_CREATE_USER)) {
        this.auth.showRightsError();
        return;
      }
      this.user = {};
      this.mode = EditViewMode.CREATE;
    } else {
      //View or Edit mode, check for the mode
      this.mode = this.route.snapshot.queryParams['mode'];
      this.canChangePassword = this.auth.canChangePassword(userId);
      if (this.mode == EditViewMode.EDIT) {
        //Edit mode, check if we're allowed to do this
        if (!this.auth.canEdit(userId)) {
          this.auth.showRightsError();
          return;
        }
        this.canEdit = true;

      } else {
        //View mode, check if we're allowed to do this
        if (!this.auth.canView(userId)) {
          this.auth.showRightsError();
          return;
        }
        this.canEdit = this.auth.canEdit(userId);
      }

      this.service.getUserById(userId)
        .subscribe(user => {
          this.user = user;
          this.copyFormControl();
        });
    }
  }

  public onSave(): void {
  }

  public onReset(): void {
    this.copyFormControl();
  }


  public getSubmitButtonText(): string {
    switch (this.mode) {
      case EditViewMode.CREATE:
        return 'Create';
      case EditViewMode.EDIT:
        return 'Save';
      case EditViewMode.VIEW:
        return '';
    }
  }

  public changeMode(before: EditViewMode, after: EditViewMode): void {
    if (before === EditViewMode.VIEW && after === EditViewMode.EDIT) {
      this.copyFormControl();
      this.mode = EditViewMode.EDIT;
    }
    if (before === EditViewMode.EDIT && after === EditViewMode.VIEW) {
      this.mode = EditViewMode.VIEW;
    }
  }

  public changePassword(): void {
    const dialogRef = this.dialog.open(PasswordChangeDialogComponent, {
      data: this.user
    });
    dialogRef.afterClosed().subscribe({
      next: (value: DialogResult) => {
        if (value.status === DialogResultStatus.ERROR) {
          console.log(value.message);
        }
      }
    });
  }

  private copyFormControl(): void {
    this.userForm.setValue({
      username: this.user?.username ?? '',
      email: this.user?.email ?? ''
    })
  }

  private emailValidator(): AsyncValidatorFn {
    return (fc: FormControl): Observable<ValidationErrors | null> => {
      return this.service.isEmailAvailable(fc.value, [this.user?.email ?? ''], 'body')
        .pipe(map((isAvailable) => (!isAvailable ? {used: true} : null)));
    };
  }

  private usernameValidator(): AsyncValidatorFn {
    return (fc: FormControl): Observable<ValidationErrors | null> => {
      return this.service.isUsernameAvailable(fc.value, [this.user?.username ?? ''], 'body')
        .pipe(map((isAvailable) => (!isAvailable ? {used: true} : null)));
    };
  }
}
