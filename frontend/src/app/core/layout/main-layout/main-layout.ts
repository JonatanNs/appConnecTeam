import {Component, inject} from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Header } from '../header/header';
import { Footer } from '../footer/footer';
import {Sidebar} from '../../../shared/components/sidebar/sidebar';
import {FlashMessage} from '../../../shared/components/flash-message/flash-message';
import {
  ListConversation
} from '../../../features/messaging/pages/conversation-page/components/list-conversation/list-conversation';
import { DockMobile } from '../../../shared/components/dock-mobile/dock-mobile';
import {AuthService} from '../../../features/auth/service/auth-service';

@Component({
  selector: 'app-main-layout',
  imports: [RouterOutlet, Header, Footer, Sidebar, FlashMessage, ListConversation, DockMobile],
  templateUrl: './main-layout.html',
  styleUrl: './main-layout.css',
})
export class MainLayout {
  private router = inject(Router);

  get currentRoute(): string {
    return this.router.url;
  }
}
