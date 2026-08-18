import {inject, Service} from '@angular/core';
import {WebSocketService} from '../../../../core/websocket/services/websocket-service';

@Service()
export class MessageService {
  private wsService = inject(WebSocketService);

  sendMessage(conversationId: string, content: string): void {
    this.wsService.sendMessage(conversationId, content);
  }

  subscribeToConversation(conversationId: string) {
    return this.wsService.subscribeToConversation(conversationId);
  }
}
