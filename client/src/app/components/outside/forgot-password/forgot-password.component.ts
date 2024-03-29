import {Component} from '@angular/core';
import {ForgotPasswordDto, UserService} from "../../../../api";
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.less'],
})
export class ForgotPasswordComponent {
  forgotPasswordDto: ForgotPasswordDto = {
    email: '',
    username: '',
    lang: 'en'
  };

  forgotForm = new FormGroup(
    {
      username: new FormControl(
        '',
        [Validators.required, Validators.minLength(3)]
      ),
      email: new FormControl(
        '',
        [Validators.required, Validators.email]
      )
    }
  );

  constructor(private userService: UserService, private router: Router, private translate: TranslateService) {
  }

  forgotPassword() {
    let forgotPasswordDto: ForgotPasswordDto = {
      username: this.forgotForm.get('username')?.value || '',
      email: this.forgotForm.get('email')?.value || '',
      lang: this.translate.currentLang
    };
    this.userService
      .forgotPassword(forgotPasswordDto, 'response')
      .subscribe((value) => {
        if (value.status === 200) {
          this.router.navigateByUrl('forgotPassword/success');
        } else {
          this.router.navigateByUrl('forgotPassword/error');
        }
      });
  }
}
