import { Component } from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterLink} from '@angular/router';

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
  protected userForm: any;
}
