import { Component, OnInit } from '@angular/core';
import { DefaultService } from '../../api';

@Component({
  selector: 'app-internal',
  templateUrl: './internal.component.html',
  styleUrls: ['./internal.component.css']
})
export class InternalComponent implements OnInit {

  greeting:string;

  constructor(private apiService: DefaultService) { }

  ngOnInit() {
    this.apiService.getInternalMessage().subscribe(data =>{
      this.greeting = data.message;
    });
  }

}
