export interface IUser {

    publicId: string,
    createdAt: Date,
    updatedAt: Date,
    firstname: string;
    lastname: string;
    email: string;
    active: true,
  roles: string[];
  address: string[];
}

