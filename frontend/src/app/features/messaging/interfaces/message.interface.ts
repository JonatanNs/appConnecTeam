export interface IMessageSend {
  publicId: string;
  senderPublicId: string;
  senderName : string
  content: string;
  createdAt: Date;
  type : "CHAT" | "SYSTEM";

}


