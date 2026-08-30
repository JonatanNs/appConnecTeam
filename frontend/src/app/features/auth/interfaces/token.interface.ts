export interface IToken {
  /**
   * userId
   */
  user : number;
  token : string;
  refreshToken : string;
  revoked : boolean;
  createdAt : string;
  expiresAt : string;

}
