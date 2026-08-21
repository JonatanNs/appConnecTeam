export interface INotification {
  publicId: string;
  content: string;
  read: boolean;
  readAt?: Date;
  type: 'NEW_MESSAGE' | 'ADDED_TO_CONVERSATION' | 'REMOVED_FROM_CONVERSATION';
  conversationPublicId?: string;
  messagePublicId?: string;
  senderName: string;
  createdAt: Date;
}
