import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DefaultService } from 'src/app/api';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-page',
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent implements OnInit {
  
  username: string;
  password: string;

  constructor(private apiService: DefaultService, private router : Router) { }

  ngOnInit() {
  }

  onSubmit() {
    this.apiService.configuration.username = this.username;
    this.apiService.configuration.password = this.password;
    this.router.navigate(['/internal/']);
  }

}
