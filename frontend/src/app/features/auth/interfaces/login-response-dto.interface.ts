export interface ILoginResponseDTO{
  id: number;
  firstname: string;
  lastname: string;
  email: string;
  online: boolean;
  token : string;
  refreshToken : string;
}
