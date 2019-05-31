import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { ApiModule, Configuration, ConfigurationParameters } from './api';
import { BASE_PATH } from './api';

import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { InternalComponent } from './components/internal/internal.component';

import { LoggedOutHttpInterceptor } from './loggedOutHttp.interceptor';
import { LoginPageComponent } from './components/login-page/login-page.component';

@NgModule({
  declarations: [
    AppComponent,
    InternalComponent,
    LoginPageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ApiModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [
    {
      provide: BASE_PATH,
      useValue: 'http://localhost:8080'
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoggedOutHttpInterceptor,
      multi: true
    }
  ],
  bootstrap: [ AppComponent ]
  })
export class AppModule { }
