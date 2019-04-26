import { Component } from '@angular/core';
import {DefaultService, MessageDTO} from './api';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
  title = 'Base Application';
  subtitle:string;
  message: string;
  dbMessage: string;
  messages: MessageDTO[];
   constructor(private apiService: DefaultService)
   {
    apiService.getTestMessage().subscribe(data =>{
      this.message = data.message;
    });
    apiService.getAppName().subscribe(data =>{
      this.subtitle = 'The name of this Application is ' + data.message;
    });
    apiService.getDB().subscribe(data =>{
      this.dbMessage = 'The database was created with the new message ' + data.message;
    });
    apiService.getAll().subscribe(data =>{
      this.messages = data;
    })
   }
}
