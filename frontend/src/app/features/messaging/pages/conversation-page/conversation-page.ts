import { Component } from '@angular/core';
import { ListConversation } from './components/list-conversation/list-conversation';

@Component({
  selector: 'app-conversation-page',
  imports: [ListConversation],
  templateUrl: './conversation-page.html',
  styleUrl: './conversation-page.css',
})
export class ConversationPage {}
