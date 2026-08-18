import { Routes } from '@angular/router';
import { ConversationPage } from './pages/conversation-page/conversation-page';

export const MESSAGING_ROUTES: Routes = [
  { path: '', component: ConversationPage, title: 'Messageries' },
];
