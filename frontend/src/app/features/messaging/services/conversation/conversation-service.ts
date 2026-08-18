import {inject, Service} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ENVIRONMENT} from '../../../../environments/environement';
import {Observable} from 'rxjs';
import {IApiResponse} from '../../../../shared/interfaces/api-response.interface';
import {IConversation} from '../../interfaces/conversation.interface';
import {IPage} from '../../../../shared/interfaces/pageable/page.interface';
import {IPageable} from '../../../../shared/interfaces/pageable/pageable.interface';

@Service()
export class CnnversationService {

  private http = inject(HttpClient);
  private baseUrl = ENVIRONMENT.apiUrl;

  createConversation(conversation : IConversation) : Observable<IApiResponse<IConversation>>{
    return this.http.post<IApiResponse<IConversation>>(`${this.baseUrl}/conversations'`, conversation)
  }

  updateConversation(publicId : string, conversation : IConversation) : Observable<IApiResponse<IConversation>>{
    return this.http.put<IApiResponse<IConversation>>(`${this.baseUrl}/conversations/${publicId}`, conversation)
  }

  deleteConversation(publicId : string) : Observable<IApiResponse<IConversation>>{
    return this.http.delete<IApiResponse<IConversation>>(`${this.baseUrl}/conversations/${publicId}`)
  }

  searchConversation(word : string, pageable : IPageable) : Observable<IApiResponse<IPage<IConversation>>>{
    return this.http.get<IApiResponse<IPage<IConversation>>>(
      `${this.baseUrl}/conversations/search?${word}?page=${pageable.page}&size=${pageable.size}`)
  }

  getConversationByUserId(publicId : string, pageable : IPageable) : Observable<IApiResponse<IPage<IConversation>>>{
    return this.http.get<IApiResponse<IPage<IConversation>>>(
      `${this.baseUrl}/conversations/users/${publicId}?page=${pageable.page}&size=${pageable.size}`)
  }



}
