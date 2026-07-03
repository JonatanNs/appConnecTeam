import { Routes } from '@angular/router';
import { DIRECTORY_ROUTES } from './features/directory/directory.routes';
import { MainLayout } from './core/layout/main-layout/main-layout';

export const routes: Routes = [
  { path: '', component: MainLayout, children: DIRECTORY_ROUTES },

    //{ path: "users",loadChildren: () =>import('').then(m => m.UsersModule) },

    // redirection si URL vide
    { path: '', redirectTo: '', pathMatch: 'full' },
];

