import { Routes } from '@angular/router';
import { DIRECTORY_ROUTES } from './features/directory/directory.routes';
import { MainLayout } from './core/layout/main-layout/main-layout';
import {ADMIN_ROUTES} from './features/admin/admin.routes';
import {LoginPage} from './features/auth/page/login-page/login-page';
import { MESSAGING_ROUTES } from './features/messaging/messaging.route';
import { NEWS_ROUTES } from './features/news/news.route';
import { AuthGuard } from './core/guard/authGuard';

export const routes: Routes = [
  { path: 'auth/connexion', component: LoginPage },
  { path: 'annuaires', component: MainLayout, canActivate :[AuthGuard], children: DIRECTORY_ROUTES },
  { path: 'admin', component: MainLayout, canActivate :[AuthGuard],  children: ADMIN_ROUTES },
  { path: 'messageries', component: MainLayout, canActivate :[AuthGuard],  children: MESSAGING_ROUTES },
  { path: 'actualites', component: MainLayout, canActivate :[AuthGuard],  children: NEWS_ROUTES },
  { path: '', redirectTo: 'auth/connexion', pathMatch: 'full' },
  // { path: '**', component: NotFoundPage }, // à ajouter si tu n'as pas encore de catch-all
];


