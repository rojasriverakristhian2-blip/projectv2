import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Presidente } from './persona/presidente/presidente';
import { Residente } from './persona/residente/residente';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'presidente', component: Presidente },
  { path: 'residente', component: Residente },
];
