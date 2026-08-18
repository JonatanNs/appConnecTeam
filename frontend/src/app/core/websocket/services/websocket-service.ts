import {Injectable, Service} from '@angular/core';
import { Client, IMessage, IMessage as StompMessage, StompSubscription } from '@stomp/stompjs';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { INotification } from '../../../features/messaging/interfaces/notification.interface';
import {ENVIRONMENT} from '../../../environments/environement';

@Service()
export class WebSocketService {
  private client: Client | null = null;

  private connected$ = new BehaviorSubject<boolean>(false);
  private authFailed$ = new Subject<string>();

  connect(): void {
    if (this.client?.active) {
      return;
    }

    this.client = new Client({
      brokerURL: ENVIRONMENT.wsUrl,
      reconnectDelay: 5000,
      onConnect: () => this.connected$.next(true),
      onStompError: (frame) => {
        const errorMsg = frame.headers['message'] ?? 'Erreur STOMP inconnue';

        if (this.isAuthError(errorMsg)) {
          this.client!.reconnectDelay = 0;
          this.authFailed$.next(errorMsg);
        }
      },
    });

    this.client.activate();
  }

  disconnect(): void {
    this.client?.deactivate();
    this.client = null;
    this.connected$.next(false);
  }

  get onAuthFailure(): Observable<string> {
    return this.authFailed$.asObservable();
  }

  get onConnectionChange(): Observable<boolean> {
    return this.connected$.asObservable();
  }

  subscribeToConversation(conversationId: string): Observable<IMessage> {
    return this.createTopicObservable<IMessage>(`/topic/conversations/${conversationId}`);
  }

  subscribeToTyping(conversationId: string): Observable<unknown> {
    return this.createTopicObservable(`/topic/conversations/${conversationId}/typing`);
  }

  subscribeToNotifications(): Observable<INotification> {
    return this.createTopicObservable<INotification>('/user/queue/notifications');
  }

  sendMessage(conversationId: string, content: string): void {
    this.publish(`/app/conversations/${conversationId}/send`, { content });
  }

  sendTyping(conversationId: string): void {
    this.publish(`/app/conversations/${conversationId}/typing`, {});
  }

  joinConversation(conversationId: string): void {
    this.publish(`/app/conversations/${conversationId}/join`);
  }

  leaveConversation(conversationId: string): void {
    this.publish(`/app/conversations/${conversationId}/leave`);
  }

  private createTopicObservable<T>(destination: string): Observable<T> {
    return new Observable<T>((subscriber) => {
      let stompSub: StompSubscription | null = null;
      let cancelled = false;

      const doSubscribe = () => {
        if (cancelled || !this.client) {
          return;
        }
        stompSub = this.client.subscribe(destination, (frame: StompMessage) => {
          subscriber.next(JSON.parse(frame.body));
        });
      };

      let connSub = this.connected$.subscribe((isConnected) => {
        if (isConnected && !stompSub) {
          doSubscribe();
        }
      });

      return () => {
        cancelled = true;
        connSub.unsubscribe();
        stompSub?.unsubscribe();
      };
    });
  }

  private publish(destination: string, body?: unknown): void {
    if (!this.client?.active) {
      console.warn(`Impossible d'envoyer sur ${destination} : WebSocket non connecté.`);
      return;
    }
    this.client.publish({
      destination,
      body: body !== undefined ? JSON.stringify(body) : undefined,
    });
  }

  private isAuthError(message: string): boolean {
    const lower = message.toLowerCase();
    return lower.includes('authentification') || lower.includes('token') || lower.includes('participant');
  }
}
