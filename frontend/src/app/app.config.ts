import {
  ApplicationConfig,
  provideAppInitializer,
  inject,
  provideBrowserGlobalErrorListeners,
} from '@angular/core';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { firstValueFrom, catchError, of } from 'rxjs';
import { routes } from './app.routes';
import { AuthService } from './features/auth/service/auth.service';
import { credentialsInterceptor } from './core/interceptors/credentials.interceptor';
import {flashMessageInterceptor} from './core/interceptors/flash-message.interceptor';
import {errorInterceptor} from './core/interceptors/error.interceptor';
import {refreshTokenInterceptor} from './core/interceptors/refresh-token.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(
      withInterceptors([
        credentialsInterceptor,
        flashMessageInterceptor,
        errorInterceptor,
        refreshTokenInterceptor
      ])
    ),
    provideRouter(routes, withComponentInputBinding()),
    provideBrowserGlobalErrorListeners(),

    provideAppInitializer(() => {
      const authService = inject(AuthService);
      return firstValueFrom(authService.fetchCurrentUser().pipe(catchError(() => of(null))));
    }),
  ],
};
