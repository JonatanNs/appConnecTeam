import { Routes } from '@angular/router';
import { Notifications } from './pages/notification/notifications';

export const NOTIFICATIONS_ROUTES: Routes = [
  { path: '', component: Notifications, title: 'Mes notification' }
];
