import { Routes } from '@angular/router';
import { ConversationPage } from './pages/conversation-page/conversation-page';
import {
  NoConversationSelected
} from './pages/conversation-page/components/no-conversation-selected/no-conversation-selected';

export const MESSAGING_ROUTES: Routes = [
  { path: '', component: NoConversationSelected, title: 'Messageries' },
  { path: 'conversation/:conversationId', component: ConversationPage, title: 'Conversation' },
];
