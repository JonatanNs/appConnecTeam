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

export const appConfig: ApplicationConfig = {
  providers: [
    provideHttpClient(withInterceptors([credentialsInterceptor])),
    provideRouter(routes, withComponentInputBinding()),
    provideBrowserGlobalErrorListeners(),

    provideAppInitializer(() => {
      const authService = inject(AuthService);
      return firstValueFrom(authService.fetchCurrentUser().pipe(catchError(() => of(null))));
    }),
  ],
};
