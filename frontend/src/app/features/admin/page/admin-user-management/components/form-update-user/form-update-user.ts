import { Component } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-form-update-user',
    imports: [
        FormsModule,
        ReactiveFormsModule,
        RouterLink
    ],
  templateUrl: './form-update-user.html',
  styleUrl: './form-update-user.css',
})
export class FormUpdateUser {}
