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
import { AuthService } from './features/auth/service/auth-service';
import { credentialsInterceptor } from './core/interceptors/credentialsInterceptor';
import {flashMessageInterceptor} from './core/interceptors/flashMessageInterceptor';
import {errorInterceptor} from './core/interceptors/errorInterceptor';
import {refreshTokenInterceptor} from './core/interceptors/refreshTokenInterceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    // app.config.ts
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
