import { Routes } from '@angular/router';
import { NewsPage } from '../news/pages/news-page/news-page';
import { Notifications } from './pages/notifications/notifications';

export const NOTIFICATIONS_ROUTES: Routes = [
  { path: '', component: Notifications, title: 'Mes notifications' }
];
