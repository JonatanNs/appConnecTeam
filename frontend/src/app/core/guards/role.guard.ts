// role.guard.ts
import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../features/auth/service/auth.service';
import {IRole} from '../../shared/interfaces/role.interface';

export const RoleGuard = (allowedRoles: string[]): CanActivateFn => {
  return () => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const user = authService.currentUser();
    const hasRole = user?.roles?.some((role: IRole) => allowedRoles.includes(role.name));

    if (hasRole) {
      return true;
    }

    router.navigate(['/']);
    return false;
  };
};
