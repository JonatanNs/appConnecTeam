import { Routes } from '@angular/router';
import { NewsPage } from './pages/news-page/news-page';

export const NEWS_ROUTES: Routes = [
  { path: '', component: NewsPage, title: 'Actualités' },
];
