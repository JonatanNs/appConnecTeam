export interface ILoginResponseDTO{
  publicId: string;
  firstname: string;
  lastname: string;
  email: string;
  online: boolean;
  token : string;
  refreshToken : string;
  tokenExpiresIn: number;
}
