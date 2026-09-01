import { Routes } from '@angular/router';
import { DIRECTORY_ROUTES } from './features/directory/directory.routes';
import { MainLayout } from './core/layout/main-layout/main-layout';
import {ADMIN_ROUTES} from './features/admin/admin.routes';
import {LoginPage} from './features/auth/page/login-page/login-page';
import { MESSAGING_ROUTES } from './features/messaging/messaging.route';
import { NEWS_ROUTES } from './features/news/news.route';
import { AuthGuard } from './core/guards/auth.guard';
import { NOTIFICATIONS_ROUTES } from './features/notifications/notification.route';
import {RoleGuard} from './core/guards/role.guard';
import {HOME_ROUTES} from './features/home/home.route';

export const routes: Routes = [
  { path: 'auth/connexion', component: LoginPage },
  { path: '', component: MainLayout, canActivate :[AuthGuard], children: HOME_ROUTES },
  { path: 'accueil', component: MainLayout, canActivate :[AuthGuard], children: HOME_ROUTES },
  { path: 'annuaires', component: MainLayout, canActivate :[AuthGuard], children: DIRECTORY_ROUTES },
  { path: 'admin', component: MainLayout, canActivate: [AuthGuard, RoleGuard(['ROLE_ADMIN'])], children: ADMIN_ROUTES },
  { path: 'messageries', component: MainLayout, canActivate :[AuthGuard],  children: MESSAGING_ROUTES },
  { path: 'actualites', component: MainLayout, canActivate :[AuthGuard],  children: NEWS_ROUTES },
  { path: 'mes-notifications', component: MainLayout, canActivate :[AuthGuard],  children: NOTIFICATIONS_ROUTES },
  { path: '', redirectTo: 'accueil', pathMatch: 'full' },
  // { path: '**', component: NotFoundPage }, // à ajouter si pas de catch-all
];


