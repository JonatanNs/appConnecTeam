import {inject, Service} from '@angular/core';
import {WebSocketService} from '../../../../core/websocket/services/websocket-service';
import { IMessageSend } from '../../interfaces/message.interface';
import { map, Observable } from 'rxjs';
import { HttpClient, HttpParams } from '@angular/common/http';
import { IApiResponse } from '../../../../shared/interfaces/api-response.interface';
import { ENVIRONMENT } from '../../../../environments/environement';

@Service()
export class MessageService {
  private wsService = inject(WebSocketService);
  private http = inject(HttpClient);
  private baseUrl = ENVIRONMENT.apiUrl;

  sendMessage(conversationId: string, content: string): void {
    this.wsService.sendMessage(conversationId, content);
  }

  subscribeToConversation(conversationId: string): Observable<IMessageSend> {
    return this.wsService.subscribeToConversation(conversationId);
  }

  getMessageHistory(conversationId: string, before?: string, limit = 20): Observable<IMessageSend[]> {
    let params = new HttpParams().set('limit', limit.toString());
    if (before) params = params.set('before', before);

    return this.http
      .get<IApiResponse<IMessageSend[]>>(`${this.baseUrl}/conversations/${conversationId}/messages`, { params })
      .pipe(map((response) => response.data));
  }
}
