import { Routes } from '@angular/router';
import { ConversationPage } from './pages/conversation-page/conversation-page';

export const MESSAGING_ROUTES: Routes = [
  { path: 'conversation/:conversationId', component: ConversationPage, title: 'Conversation' },
];
