import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { ApiModule, Configuration, ConfigurationParameters } from './api';
import { BASE_PATH } from './api';

import { HttpClientModule } from '@angular/common/http';
import {environment} from "../environments/environment";


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ApiModule,
    HttpClientModule
  ],
  providers: [ { provide: BASE_PATH, useValue: environment.serverUrl} ],
  bootstrap: [ AppComponent ]
  })
export class AppModule { }
