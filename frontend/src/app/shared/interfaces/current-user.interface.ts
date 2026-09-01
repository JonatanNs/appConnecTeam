import { IRole } from './role.interface';

export interface ICurrentUser {
  publicId: string;
  firstname: string;
  lastname: string;
  email: string;
  online: boolean;
  roles : IRole[];
}
