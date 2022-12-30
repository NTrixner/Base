import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {UserDto, UserListDto, UserlistService} from '../../../../api';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {AuthService} from '../../../services/auth/auth.service';
import {RightsConstants} from '../../../constants/rights-constants';
import {ActivatedRoute, Router} from '@angular/router';
import {EditViewMode} from '../../../enums/edit-view-mode';
import {MatDialog} from '@angular/material/dialog';
import {UserDeleteConfirmDialogComponent} from './user-delete-confirm-dialog/user-delete-confirm-dialog.component';
import {DialogResult} from '../../../interfaces/dialog-result';
import {DialogResultStatus} from '../../../enums/dialog-result-status';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.less']
})
export class UserListComponent implements AfterViewInit {
  public RightsConstants = RightsConstants;
  displayedColumns: string[] = ['id', 'username', 'email', 'type', 'menu'];
  dataSource = new MatTableDataSource<UserDto>();

  @ViewChild(MatPaginator) paginator: MatPaginator | null = null;
  @ViewChild(MatSort) sort: MatSort | null = null;

  constructor(
    private authService: AuthService,
    private service: UserlistService,
    public router: Router,
    public route: ActivatedRoute,
    public dialog: MatDialog
  ) {
  }

  ngAfterViewInit(): void {
    if (this.canSeeUsers()) {
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      this.loadUserList();
    }
    if (!this.canSeeUsers() && !this.canCreateUsers()) {
      this.authService.showRightsError();
    }
  }

  loadUserList() {
    this.service.getUserCount('body').subscribe((num) => {
      if (this.paginator) {
        this.paginator.length = num;
      }
    });
    this.service
      .listUsers(
        this.paginator?.pageIndex,
        this.paginator?.pageSize,
        this.getOrderField(),
        this.getOrderDirection(),
        'body'
      )
      .subscribe((data: UserListDto) => {
        this.dataSource = new MatTableDataSource<UserDto>(data.items);
      });
  }

  private getOrderField(): string | undefined {
    return !this.sort?.active ? undefined : this.sort.active;
  }

  private getOrderDirection(): string | undefined {
    return !this.sort?.active ? undefined : this.sort.direction;
  }

  public canSeeUsers(): boolean {
    return this.authService.hasRight(RightsConstants.ROLE_USER_CAN_WATCH_USERLIST);
  }

  public canCreateUsers(): boolean {
    return this.authService.hasRight(RightsConstants.ROLE_USER_CAN_CREATE_USER);
  }

  public canEditUser(user: UserDto): boolean {
    return this.authService.canView(user.id!);
  }

  public canDeleteUser(user: UserDto): boolean {
    return this.canEditUser(user) && !this.authService.isCurrentUser(user.id!);
  }

  public createUser(): void {
    this.router.navigate(['/base/user'], {relativeTo: this.route, queryParams: {mode: EditViewMode.CREATE}});
  }

  public editUser(user: UserDto): void {
    this.openUser(user, EditViewMode.EDIT);
  }

  public openUser(user: UserDto, mode: EditViewMode): void {
    this.router.navigate(['/base/user'], {relativeTo: this.route, queryParams: {user: user.id, mode: mode}});
  }

  public viewUser(user: UserDto): void {
    this.openUser(user, EditViewMode.VIEW);
  }


  public deleteUser(user: UserDto): void {
    const dialogRef = this.dialog.open(UserDeleteConfirmDialogComponent, {data: user});
    dialogRef.afterClosed().subscribe({
      next: (result: DialogResult) => {
        if (result.status === DialogResultStatus.ERROR) {
          console.log(result.message);
        } else if (result.status === DialogResultStatus.OK) {
          this.loadUserList()
        }
      }
    });
  }
}
