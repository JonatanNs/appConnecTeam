import { Routes } from '@angular/router';
import {AdminPage} from './page/admin-page/admin-page';
import {AdminUserManagement} from './page/admin-user-management/admin-user-management';
import {FormAdduser} from './page/admin-user-management/components/form-adduser/form-adduser';


export const ADMIN_ROUTES: Routes = [
  { path: "", component: AdminPage, title: "Administration" },
  { path: "gestion-utilisateurs", component: AdminUserManagement, title: "Gestion des utilisateurs" },
  { path: "gestion-utilisateurs/ajouter-utilisateur", component: FormAdduser, title: "Ajouter un utilisateur" },
];
