import {Component, Input, OnInit} from '@angular/core';
import {EditViewMode} from '../../../enums/edit-view-mode';
import {UserDto, UserService, UserTypeDto} from '../../../../api';
import {ActivatedRoute, Router} from '@angular/router';
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
  public userTypes: UserTypeDto[] = [];
  public defaultType: UserTypeDto = {id: 0, name: 'USER', rights: [], isDefault: true};

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
      type: new FormControl(0, [
        Validators.required
      ])
    }
  );

  constructor(private service: UserService,
              private auth: AuthService,
              private route: ActivatedRoute,
              private router: Router,
              public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    let userId: string = this.route.snapshot.queryParams['user'] || null;
    this.mode = this.route.snapshot.queryParams['mode'];
    this.service.getUserTypes().subscribe({
      next: types => {
        this.userTypes = types;
        this.defaultType = types.find(t => t.isDefault)!
      },
      error: () => {
      }
    })
    if (!userId) {
      this.user = {};
      this.mode = EditViewMode.CREATE;
      this.checkRights();
    } else {
      this.loadUser(userId);
    }
  }

  checkRights(): void {
    if (!this.user.id) {
      //Create mode, check if we can do this
      if (!this.auth.hasRight(RightsConstants.ROLE_USER_CAN_CREATE_USER)) {
        this.auth.showRightsError();
        return;
      }
    } else {
      //View or Edit mode, check for the mode
      this.canChangePassword = this.auth.canChangePassword(this.user.id);
      if (this.mode == EditViewMode.EDIT) {
        //Edit mode, check if we're allowed to do this
        if (!this.auth.canEdit(this.user.id)) {
          this.auth.showRightsError();
          return;
        }
        this.canEdit = true;
      } else {
        //View mode, check if we're allowed to do this
        if (!this.auth.canView(this.user.id)) {
          this.auth.showRightsError();
          return;
        }
        this.canEdit = this.auth.canEdit(this.user.id);
      }
    }
  }

  isOwnUser(): boolean {
    return this.auth.getCurrentUser()?.id == this.user.id;
  }

  public onSave(): void {
    let userDto = {
      username: this.userForm.controls.username.value!,
      email: this.userForm.controls.email.value!,
      id: this.user.id,
      rights: (this.getUserType(this.userForm.controls.type.value ?? 0)).rights,
      type: this.getUserType(this.userForm.controls.type.value ?? 0)
    };
    if (this.mode == EditViewMode.EDIT) {
      this.service.changeUser(userDto).subscribe({
        next: () => {
          this.changeMode(EditViewMode.EDIT, EditViewMode.VIEW);
        },
        error: () => {
        }
      });
    } else if (this.mode == EditViewMode.CREATE) {
      this.service.createUser(userDto).subscribe({
        next: (uuid: string) => {
          this.user.id = uuid;
          this.changeMode(EditViewMode.CREATE, EditViewMode.VIEW);
        },
        error: () => {
        }
      });
    }
  }

  getUserType(id: number = 0): UserTypeDto {
    return this.userTypes.find(t => t.id === id)!;
  }

  public changeMode(before: EditViewMode, after: EditViewMode): void {
    if (before === EditViewMode.VIEW && after === EditViewMode.EDIT) {
      this.copyFormControl();
      this.mode = EditViewMode.EDIT;
    }
    if (before === EditViewMode.EDIT && after === EditViewMode.VIEW) {
      this.mode = EditViewMode.VIEW;
      this.loadUser(this.user?.id!);
    }
    if (before === EditViewMode.CREATE && after === EditViewMode.VIEW) {
      this.mode = EditViewMode.VIEW;
      this.loadUser(this.user?.id!);
    }
  }

  public onReset(): void {
    this.copyFormControl();
  }

  public canChangeType(): boolean {
    return this.auth.hasRight(RightsConstants.ROLE_USER_CAN_PROMOTE_USERS) && !this.isOwnUser();
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

  private loadUser(userId: string) {
    if (this.auth.isCurrentUser(userId)) {
      this.user = this.auth.user!;
      this.copyFormControl();
      this.checkRights();
    } else if (this.auth.hasRight(RightsConstants.ROLE_USER_CAN_GET_USER_BY_ID)) {
      this.service.getUserById(userId)
        .subscribe({
          next: user => {
            this.user = user;
            this.copyFormControl();
            this.checkRights();
          },
          error: () => {
          }
        });
    }
  }

  private copyFormControl(): void {
    this.userForm.setValue({
      username: this.user?.username ?? '',
      email: this.user?.email ?? '',
      type: this.user?.type?.id ?? this.defaultType?.id ?? 0
    })
    if (this.isOwnUser()) {
      this.userForm.controls.username.disable();
    } else {
      this.userForm.controls.username.enable();
    }
    if (!this.canChangeType()) {
      this.userForm.controls.type.disable();
    } else {
      this.userForm.controls.type.enable();
    }
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
