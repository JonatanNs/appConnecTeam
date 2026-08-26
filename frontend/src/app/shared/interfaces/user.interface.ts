export interface IUser {
  publicId: string;
  createdAt: Date;
  updatedAt: Date;
  firstname: string;
  lastname: string;
  email: string;
  active: true;
  online: boolean;
  roles: string[];
  address: string[];
}
