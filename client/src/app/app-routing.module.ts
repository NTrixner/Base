import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {AuthGuard} from './guards/auth/auth.guard';
import {ForgotPasswordComponent} from './components/forgot-password/forgot-password.component';
import {RegisterSuccessComponent} from './components/register/register-success/register-success.component';
import {RegisterErrorComponent} from './components/register/register-error/register-error.component';


const routes: Routes = [
  {path: '', component: HomeComponent, canActivate: [AuthGuard]},
  {path: 'login', component: LoginComponent},
  {path: 'register', component: RegisterComponent},
  {path: 'register/success', component: RegisterSuccessComponent},
  {path: 'register/error', component: RegisterErrorComponent},
  {path: 'forgotPassword', component: ForgotPasswordComponent},
  // otherwise redirect to home
  {path: '**', redirectTo: ''}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {

}
