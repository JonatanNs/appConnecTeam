import { Component, computed, inject, signal } from '@angular/core';
import { DatePipe, NgClass } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FaIconComponent, IconDefinition } from '@fortawesome/angular-fontawesome';
import { faBell as faBellRegular } from '@fortawesome/free-regular-svg-icons';
import { faAt, faBell as faBellSolid, faCheckDouble, faCircleExclamation, faComment, faHeart, faUserPlus } from '@fortawesome/free-solid-svg-icons';
import { NotificationService } from '../../../../../../features/notifications/services/notification/notification.service';
import { INotification } from '../../../../../../features/notifications/interfaces/notification.interface';

@Component({
  selector: 'app-user-notif',
  standalone: true,
  imports: [FaIconComponent, RouterLink, DatePipe, NgClass],
  templateUrl: './user-notif.html',
  styleUrl: './user-notif.css',
})
export class UserNotif {
  protected readonly faBell = faBellRegular;
  protected readonly faCheckDouble = faCheckDouble;

  private readonly notificationService = inject(NotificationService);

  private closeMenuTimeout?: ReturnType<typeof setTimeout>;

  isNotificationMenuOpen = signal(false);

  notifications = this.notificationService.notifications;
  unreadCount = computed(() => this.notifications().filter((n) => !n.read).length);

  /**
   * Bascule l'affichage du menu lors d'un clic sur le bouton
   */
  toggleNotificationMenu(): void {
    this.isNotificationMenuOpen.update((open) => !open);
  }

  /**
   * Ferme le menu lorsque la souris quitte le composant
   * après un court délai.
   */
  closeNotificationMenu(): void {
    this.closeMenuTimeout = setTimeout(() => {
      this.isNotificationMenuOpen.set(false);
    }, 250);
  }

  /**
   * Annule la fermeture programmée du menu.
   */
  cancelCloseNotificationMenu(): void {
    if (this.closeMenuTimeout) {
      clearTimeout(this.closeMenuTimeout);
      this.closeMenuTimeout = undefined;
    }
  }

  markAsRead(notif: INotification): void {
    if (notif.read) return;
    this.notificationService.markAsRead(notif.publicId).subscribe();
  }

  markAllAsRead(): void {
    this.notificationService.markAllAsRead().subscribe();
  }

  /**
   * Retourne l'icône FontAwesome appropriée en fonction du type de notification.
   */
  iconFor(type: string): IconDefinition {
    switch (type?.toUpperCase()) {
      case 'MESSAGE':
      case 'CHAT':
        return faComment;

      case 'FRIEND_REQUEST':
      case 'USER_FOLLOW':
      case 'INVITATION':
        return faUserPlus;

      case 'LIKE':
      case 'REACTION':
        return faHeart;

      case 'MENTION':
      case 'TAG':
        return faAt;

      case 'SYSTEM':
      case 'WARNING':
        return faCircleExclamation;

      default:
        return faBellSolid;
    }
  }
}
