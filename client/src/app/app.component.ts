import { Component } from '@angular/core';
import { DefaultService } from './api';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent{
  title = 'Base Application';
  message: string;
   constructor(private apiService: DefaultService)
   {
    apiService.getTestMessage().subscribe(data =>{
      this.message = data.message;
    });
   }
}
