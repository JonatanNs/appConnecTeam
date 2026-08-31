import {Component, inject, signal} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {email, form, FormField, FormRoot, required, schema} from '@angular/forms/signals';
import {AuthService} from '../../../../service/auth.service';
import {FlashMessageService} from '../../../../../../core/services/flashMessage/flash-message.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-form-login',
  imports: [
    ReactiveFormsModule,
    FormField,
    FormRoot
  ],
  templateUrl: './form-login.html',
  styleUrl: './form-login.css',
})
export class FormLogin {

  private authService = inject(AuthService);
  private flashMessage = inject(FlashMessageService);
  private route = inject(Router);

  errorMessage = signal<string>("");

  login = signal({ email: '@nexteam.com', password: 'Password123' });

  loginForm = form(this.login, schema((path) =>{
    required(path.email , { message : 'Le mail est requis' });
    required(path.password, { message : 'Le mot de passe est requis' });
    email(path.email, { message : 'Le mail n\'est pas valide' });
  }))

  loginSubmit(){
    if (this.loginForm().invalid()) {
      return;
    }

    this.authService.showLogin({ email: this.login().email, password: this.login().password }).subscribe({
      next: value => {
        this.route.navigate(['/annuaires']);
        this.flashMessage.success(value.message);
      },
      error: err => {
        this.errorMessage.set(err.error.message);
      }
    })
  }
}
