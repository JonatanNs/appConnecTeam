import {IUser} from '../../../shared/interfaces/user.interface';
import {IMessage} from './message.interface';

export interface IConversation {
  publicId: string;
  users: IUser[];
  name: string;
  createdAt: Date;
  messages : IMessage[] ;
}
