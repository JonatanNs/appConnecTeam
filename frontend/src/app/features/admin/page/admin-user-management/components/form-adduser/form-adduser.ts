import {Component, effect, inject, signal, Signal} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';
import {UserService} from '../../../../../../core/services/user/user-service';
import {IUser} from '../../../../../../shared/interfaces/user.interface';
import {toSignal} from '@angular/core/rxjs-interop';
import {FlashMessageService} from '../../../../../../core/services/flashMessage/flash-message-service';

@Component({
  selector: 'app-form-adduser',
  imports: [
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './form-adduser.html',
  styleUrl: './form-adduser.css',
})
export class FormAdduser {

  private userService = inject(UserService);
  protected flashService = inject(FlashMessageService);
  private router = inject(Router);

  userForm = new FormGroup({
    firstname:  new FormControl('', Validators.required),
    lastname:  new FormControl('', Validators.required),
    email:  new FormControl('', [Validators.required, Validators.email]),
  });

  firstnameSig  = toSignal(this.userForm.get('firstname')!.valueChanges, { initialValue: '' });
  lastnameSig= toSignal(this.userForm.get('lastname')!.valueChanges, { initialValue: '' });
  errorSig  = signal(null);

  constructor() {
    effect(() => {
      // @ts-ignore
      const email = `${this.firstnameSig().toLowerCase().trim()}.${this.lastnameSig().toLowerCase().trim()}@nexteam.com`;
      this.userForm.get('email')!.setValue(email, { emitEvent: false });
    });
  }

  onSubmit(){
    if (this.userForm.invalid) {
      return;
    }

    this.userService.createUser(this.userForm.value as IUser).subscribe({
      next: (res) => {
        console.log(res);
        this.router.navigate(['/admin/gestion-utilisateurs']);
      },
      error: (error) => {
        console.error(error);
        this.errorSig.set(error.error.message);
      }
    });

  }
}

