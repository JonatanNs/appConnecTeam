import {Component, inject, signal} from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import {UserService} from '../../../../../../core/services/user/user-service';
import {IUser} from '../../../../../../shared/interfaces/user.interface';
import {FlashMessageService} from '../../../../../../core/services/flashMessage/flash-message-service';
import { FaIconComponent } from '@fortawesome/angular-fontawesome';
import { faArrowLeft } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-form-update-user',
  imports: [
    FormsModule,
    ReactiveFormsModule,
    RouterLink,
    FaIconComponent
  ],
  templateUrl: './form-update-user.html',
  styleUrl: './form-update-user.css',
})
export class FormUpdateUser {

  private userService = inject(UserService);
  private route = inject(ActivatedRoute);
  private flashMessage = inject(FlashMessageService);
  private router = inject(Router);

  errorSig = signal('');

  userId!: string;


  userForm = new FormGroup({
    firstname: new FormControl('', [
      Validators.required
    ]),
    lastname: new FormControl('', [
      Validators.required
    ]),
    email: new FormControl('', [
      Validators.required,
      Validators.email
    ])
  });


  ngOnInit() {

    this.userId = this.route.snapshot.paramMap.get('publicId')!;

    this.userService.getUser(this.userId)
      .subscribe({
        next: (user) => {

          this.userForm.patchValue({
            firstname: user.data.firstname,
            lastname: user.data.lastname,
            email: user.data.email
          });

        },
        error: (err) => {
          this.errorSig.set(
            "Impossible de charger l'utilisateur."
          );
        }
      });
  }


  onSubmit() {

    if (this.userForm.invalid) {
      this.userForm.markAllAsTouched();
      return;
    }

    this.userService.updateUser(
      this.userId,
      this.userForm.value as IUser
    )
      .subscribe({
        next: (res) => {

          this.flashMessage.success(res.message);

          this.router.navigate([
            '/admin/gestion-utilisateurs'
          ]);

        },

        error: (err) => {
          const message = err.error?.message ?? "Une erreur est survenue.";

          this.flashMessage.error(message);
          this.errorSig.set(message);
        }
      });
  }

  protected readonly faArrowLeft = faArrowLeft;
}
