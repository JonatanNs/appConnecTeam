import {HttpEventType, HttpInterceptorFn} from '@angular/common/http';
import {tap} from 'rxjs';
import {FlashMessageService} from '../services/flashMessage/flash-message-service';
import {inject} from '@angular/core';

export const flashMessageInterceptor: HttpInterceptorFn = (req, next) => {
  const flashMessage = inject(FlashMessageService);
  const showSuccess = req.headers.has('X-Show-Success');

  const cleanedReq = showSuccess ? req.clone({ headers: req.headers.delete('X-Show-Success') }) : req;

  return next(cleanedReq).pipe(
    tap((event) => {
      if (showSuccess && event.type === HttpEventType.Response) {
        const body = event.body as { message?: string };
        if (body?.message) flashMessage.success(body.message);
      }
    }),
  );
};
