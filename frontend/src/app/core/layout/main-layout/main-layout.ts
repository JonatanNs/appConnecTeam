import {Component, inject} from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Header } from '../header/header';
import { Footer } from '../footer/footer';
import {Sidebar} from '../../../shared/components/sidebar/sidebar';
import {FlashMessageService} from '../../services/flashMessage/flash-message-service';
import {FlashMessage} from '../../../shared/components/flash-message/flash-message';

@Component({
  selector: 'app-main-layout',
  imports: [RouterOutlet, Header, Footer, Sidebar, FlashMessage],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {
}
