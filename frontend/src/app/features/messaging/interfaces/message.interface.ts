export interface IMessageSend {
  publicId: string;
  senderPublicId: string;
  senderName : string
  content: string;
  createdAt: string;
  type : "CHAT" | "SYSTEM";

}


