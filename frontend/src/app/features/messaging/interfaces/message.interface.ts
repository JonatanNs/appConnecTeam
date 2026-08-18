export interface IMessage {
  publicId: string;
  senderPublicId: string;
  senderName : string
  content: string;
  createdAt: Date;
  type : "CHAT" | "SYSTEM";
  messages : IMessage[] ;
}


