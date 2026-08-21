import { inject, Service } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ENVIRONMENT } from '../../../../environments/environement';
import { Observable, Subject, tap } from 'rxjs';
import { IApiResponse } from '../../../../shared/interfaces/api-response.interface';
import { IConversation } from '../../interfaces/conversation.interface';
import { IPage } from '../../../../shared/interfaces/pageable/page.interface';
import { IPageable } from '../../../../shared/interfaces/pageable/pageable.interface';
import { ICreateConversation } from '../../interfaces/create-conversation.interface';

@Service()
export class ConversationService {
  private http = inject(HttpClient);
  private baseUrl = ENVIRONMENT.apiUrl;

  // ConversationService
  private conversationCreated$ = new Subject<void>();
  readonly onConversationCreated = this.conversationCreated$.asObservable();

  createConversation(conversation: ICreateConversation): Observable<IApiResponse<IConversation>> {
    return this.http
      .post<IApiResponse<IConversation>>(`${this.baseUrl}/conversations`, conversation)
      .pipe(tap(() => this.conversationCreated$.next()));
  }

  getConversation(publicId: string): Observable<IApiResponse<IConversation>> {
    return this.http.get<IApiResponse<IConversation>>(`${this.baseUrl}/conversations/${publicId}`);
  }

  getConversationByUserId(
    publicId: string,
    pageable: IPageable,
  ): Observable<IApiResponse<IPage<IConversation>>> {
    return this.http.get<IApiResponse<IPage<IConversation>>>(
      `${this.baseUrl}/conversations/users/${publicId}?page=${pageable.page}&size=${pageable.size}`,
    );
  }

  searchConversation(
    word: string,
    pageable: IPageable,
  ): Observable<IApiResponse<IPage<IConversation>>> {
    const params = new HttpParams()
      .set('word', word)
      .set('page', pageable.page.toString())
      .set('size', pageable.size.toString());

    return this.http.get<IApiResponse<IPage<IConversation>>>(
      `${this.baseUrl}/conversations/search`,
      { params },
    );
  }

  private conversationUpdated$ = new Subject<void>();
  readonly onConversationUpdated = this.conversationUpdated$.asObservable();

  updateConversation(
    publicId: string,
    conversation: ICreateConversation,
  ): Observable<IApiResponse<ICreateConversation>> {
    return this.http
      .put<IApiResponse<ICreateConversation>>(
        `${this.baseUrl}/conversations/${publicId}`,
        conversation,
      )
      .pipe(tap(() => this.conversationUpdated$.next()));
  }

  private conversationLeftOrDeleted$ = new Subject<string>();
  readonly onConversationLeftOrDeleted = this.conversationLeftOrDeleted$.asObservable();

  deleteConversation(publicId: string): Observable<IApiResponse<IConversation>> {
    return this.http
      .delete<IApiResponse<IConversation>>(`${this.baseUrl}/conversations/${publicId}`)
      .pipe(tap(() => this.conversationLeftOrDeleted$.next(publicId)));
  }

  leaveConversation(conversationId: string): Observable<IApiResponse<void>> {
    return this.http
      .delete<IApiResponse<void>>(`${this.baseUrl}/conversations/${conversationId}/leave`)
      .pipe(tap(() => this.conversationLeftOrDeleted$.next(conversationId)));
  }
}
