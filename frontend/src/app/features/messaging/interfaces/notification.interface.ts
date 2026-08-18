export enum NotificationType {
  NEW_MESSAGE = 'NEW_MESSAGE',
  ADDED_TO_CONVERSATION = 'ADDED_TO_CONVERSATION',
  REMOVED_FROM_CONVERSATION = 'REMOVED_FROM_CONVERSATION'
}

export interface INotification {
  publicId: string;
  content: string;
  read: boolean;
  readAt?: Date;
  type: NotificationType;
  conversationPublicId?: string;
  messagePublicId?: string;
  senderName: string;
  createdAt: Date;
}
