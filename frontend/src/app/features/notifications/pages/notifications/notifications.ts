import { Component, inject } from '@angular/core';
import { NotificationService } from '../../services/notification/notification-service';
import { WebSocketService } from '../../../../core/websocket/services/websocket-service';

@Component({
  selector: 'app-notifications',
  imports: [],
  templateUrl: './notifications.html',
  styleUrl: './notifications.css',
})
export class Notifications {

  private notificationsService = inject(NotificationService);
  private wsService = inject(WebSocketService);

  constructor() {
    
  }
}
