import {HttpInterceptorFn} from '@angular/common/http';
import {inject} from '@angular/core';
import {FlashMessageService} from '../services/flashMessage/flash-message-service';
import {catchError, throwError} from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const flashMessage = inject(FlashMessageService);

  return next(req).pipe(
    catchError((error) => {
      const message = error?.error?.message ?? 'Une erreur est survenue.';
      flashMessage.error(message);
      return throwError(() => error);
    }),
  );
};
