import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError, BehaviorSubject, filter, take } from 'rxjs';
import { AuthService } from '../../features/auth/service/auth.service';

let isRefreshing = false;
const refreshComplete$ = new BehaviorSubject<boolean>(true);

export const refreshTokenInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  // Ne pas intercepter les appels vers /auth (login, refresh lui-même) pour éviter les boucles
  if (req.url.includes('/auth/login') || req.url.includes('/auth/refresh')) {
    return next(req);
  }

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status !== 401) {
        return throwError(() => error);
      }

      if (!isRefreshing) {
        isRefreshing = true;
        refreshComplete$.next(false);

        return authService.refreshToken().pipe(
          switchMap(() => {
            isRefreshing = false;
            refreshComplete$.next(true);
            return next(req); // rejoue la requête originale
          }),
          catchError((refreshError) => {
            isRefreshing = false;
            refreshComplete$.next(true);
            authService.logout(); // refresh échoué → vraie déconnexion
            return throwError(() => refreshError);
          }),
        );
      }

      // Un refresh est déjà en cours → attendre qu'il se termine, puis rejouer
      return refreshComplete$.pipe(
        filter((done) => done),
        take(1),
        switchMap(() => next(req)),
      );
    }),
  );
};
