import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ApiModule, Configuration, ConfigurationParameters } from '../api';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './components/outside/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { RegisterComponent } from './components/outside/register/register.component';
import { ForgotPasswordComponent } from './components/outside/forgot-password/forgot-password.component';
import { environment } from '../environments/environment';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { RegisterSuccessComponent } from './components/outside/register/register-success/register-success.component';
import { RegisterErrorComponent } from './components/outside/register/register-error/register-error.component';
import { ForgotPasswordSuccessComponent } from './components/outside/forgot-password/forgot-password-success-component/forgot-password-success.component';
import { ForgotPasswordErrorComponent } from './components/outside/forgot-password/forgot-password-error-component/forgot-password-error.component';
import { ResetPasswordComponent } from './components/outside/reset-password/reset-password.component';
import {
  ResetPasswordSuccessComponent
} from './components/outside/reset-password/reset-password-success-component/reset-password-success.component';
import {
  ResetPasswordErrorComponent
} from './components/outside/reset-password/reset-password-error-component/reset-password-error.component';

export function apiConfigFactory(): Configuration {
  const params: ConfigurationParameters = {
    basePath: environment.serverUrl,
  };
  return new Configuration(params);
}

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    RegisterComponent,
    ForgotPasswordComponent,
    RegisterSuccessComponent,
    RegisterErrorComponent,
    ForgotPasswordSuccessComponent,
    ForgotPasswordErrorComponent,
    ResetPasswordComponent,
    ResetPasswordSuccessComponent,
    ResetPasswordErrorComponent
  ],
  imports: [
    ApiModule,
    ApiModule.forRoot(apiConfigFactory),
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatCardModule,
    MatFormFieldModule,
    MatIconModule,
    MatButtonModule,
    MatInputModule,
    ReactiveFormsModule,
    FormsModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
