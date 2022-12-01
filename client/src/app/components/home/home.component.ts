import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {AuthService} from '../../services/auth/auth.service';
import {UserDto, UserListDto, UserlistService} from '../../../api';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {RightsConstants} from '../../constants/rights-constants';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.less'],
})
export class HomeComponent implements AfterViewInit {
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

  logout(): void {
    this.authService.logout();
  }

  canSeeUsers(): boolean {
    return this.authService.hasRight(RightsConstants.ROLE_USER_CAN_WATCH_USERLIST);
  }
}
