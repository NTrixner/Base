import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { ApiModule, Configuration, ConfigurationParameters } from './api';
import { BASE_PATH } from './api';

import { HttpClientModule } from '@angular/common/http';


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
  providers: [ { provide: BASE_PATH, useValue: 'http://localhost:8080' } ],
  bootstrap: [ AppComponent ]
  })
export class AppModule { }
