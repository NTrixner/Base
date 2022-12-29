import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/inside/home/home.component';
import {LoginComponent} from './components/outside/login/login.component';
import {RegisterComponent} from './components/outside/register/register.component';
import {AuthGuard} from './guards/auth/auth.guard';
import {ForgotPasswordComponent} from './components/outside/forgot-password/forgot-password.component';
import {RegisterSuccessComponent} from './components/outside/register/register-success/register-success.component';
import {RegisterErrorComponent} from './components/outside/register/register-error/register-error.component';
import {
  ForgotPasswordSuccessComponent
} from './components/outside/forgot-password/forgot-password-success-component/forgot-password-success.component';
import {
  ForgotPasswordErrorComponent
} from './components/outside/forgot-password/forgot-password-error-component/forgot-password-error.component';
import {ResetPasswordComponent} from './components/outside/reset-password/reset-password.component';
import {
  ResetPasswordSuccessComponent
} from './components/outside/reset-password/reset-password-success-component/reset-password-success.component';
import {
  ResetPasswordErrorComponent
} from './components/outside/reset-password/reset-password-error-component/reset-password-error.component';
import {UserListComponent} from './components/inside/user-list/user-list.component';
import {BaseComponent} from './components/inside/base/base.component';

const routes: Routes = [
  {path: '', component: LoginComponent},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'register/success', component: RegisterSuccessComponent},
  {path: 'register/error', component: RegisterErrorComponent},
  {path: 'forgotPassword', component: ForgotPasswordComponent},
  {path: 'forgotPassword/success', component: ForgotPasswordSuccessComponent},
  {path: 'forgotPassword/error', component: ForgotPasswordErrorComponent},
  {path: 'resetPassword', component: ResetPasswordComponent},
  {path: 'resetPassword/success', component: ResetPasswordSuccessComponent},
  {path: 'resetPassword/error', component: ResetPasswordErrorComponent},
  {path: 'base', component: BaseComponent, canActivate: [AuthGuard], children: [
      {path: 'home', component: HomeComponent, canActivate: [AuthGuard]},
      {path: 'userlist', component: UserListComponent, canActivate: [AuthGuard]},
    ]},
  // otherwise redirect to home
  {path: '**', redirectTo: ''},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {
}
