import { Routes } from '@angular/router';
import { DIRECTORY_ROUTES } from './features/directory/directory.routes';
import { MainLayout } from './core/layout/main-layout/main-layout';
import {ADMIN_ROUTES} from './features/admin/admin.routes';
import {LoginPage} from './features/auth/page/login-page/login-page';

export const routes: Routes = [
  { path: 'auth/connexion', component: LoginPage },
  { path: 'annuaires', component: MainLayout, children: DIRECTORY_ROUTES },
  { path: 'admin', component: MainLayout, children: ADMIN_ROUTES },
  { path: '', redirectTo: 'auth/connexion', pathMatch: 'full' },
  // { path: '**', component: NotFoundPage }, // à ajouter si tu n'as pas encore de catch-all
];


