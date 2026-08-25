import { Component } from '@angular/core';
import { Navbar } from './navbar/navbar';
import {NgOptimizedImage} from '@angular/common';
import {faBars} from '@fortawesome/free-solid-svg-icons/faBars';

@Component({
  selector: 'app-header',
  imports: [Navbar, NgOptimizedImage],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  protected readonly faBars = faBars;
}
