export interface IUser {

    id?: number,
    version: number,
    publicId: string,
    createdAt: Date,
    updatedAt: Date,
    firstname: string;
    lastname: string;
    email: string;
    password: string;
    active: true,
    roles: [],
    address: []

}

