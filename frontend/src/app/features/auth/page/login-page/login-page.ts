import {Component, OnInit, signal} from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import {FormLogin} from './components/form-login/form-login';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [ReactiveFormsModule, FormLogin],
  templateUrl: './login-page.html',
  styleUrl: './login-page.css',
})
export class LoginPage implements OnInit {
  protected readonly mosaicColors = signal<string[]>([]);
  ngOnInit(): void {
    // Dégradé inspiré des variables --nxt-blue et --nxt-blue-sky
    const palette = [
      '#001b73', '#002ec7', '#003eff', '#3365ff',
      '#5280ff', '#6f9ff8', '#8eb3f9', '#adcafd',
      '#d1e2fe', '#e8f0fe',
    ];

    this.mosaicColors.set(
      Array.from({ length: 250 }, () => palette[Math.floor(Math.random() * palette.length)])
    );
  }
}
