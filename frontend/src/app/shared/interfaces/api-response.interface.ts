export interface IApiResponse<T> {
    status: number;
    code: string;
    message: string;
    data: T[];
}
