import {IUser} from '../../../shared/interfaces/user.interface';
import { IMessageSend } from './message.interface';

export interface IConversation {
  publicId: string;
  users: IUser[];
  name: string;
  createdAt: Date;
  messages : IMessageSend[] ;
}
