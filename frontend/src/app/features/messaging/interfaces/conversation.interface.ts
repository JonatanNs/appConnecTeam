import {IUser} from '../../../shared/interfaces/user.interface';
import { IMessageSend } from './message.interface';

export interface IConversation {
  publicId: string;
  users: IUser[];
  name: string;
  owner : IUser;
  createdAt: Date;
  messages : IMessageSend[] ;
}
