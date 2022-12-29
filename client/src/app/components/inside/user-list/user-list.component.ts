import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {UserDto, UserListDto, UserlistService} from '../../../../api';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {AuthService} from '../../../services/auth/auth.service';
import {RightsConstants} from '../../../constants/rights-constants';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.less']
})
export class UserListComponent implements AfterViewInit {
  displayedColumns: string[] = ['id', 'username', 'email'];
  dataSource = new MatTableDataSource<UserDto>();

  @ViewChild(MatPaginator) paginator: MatPaginator | null = null;
  @ViewChild(MatSort) sort: MatSort | null = null;

  constructor(
    private authService: AuthService,
    private service: UserlistService
  ) {
  }

  ngAfterViewInit(): void {
    if(this.canSeeUsers())
    {
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
      this.loadUserList();
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

  getUsername(): string {
    return this.authService.user?.username || 'Anonymous';
  }


  canSeeUsers(): boolean {
    return this.authService.hasRight(RightsConstants.ROLE_USER_CAN_WATCH_USERLIST);
  }
}
