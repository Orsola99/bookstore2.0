import { Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { ProdottiComponent } from './components/prodotti/prodotti.component';
import { ProdottoDettaglioComponent } from './components/prodotti/prodotto-dettaglio/prodotto-dettaglio.component';
import { LoginComponent } from './components/auth/login/login.component';
import { RegisterComponent } from './components/auth/register/register.component';
import { CarrelloComponent } from './components/carrello/carrello.component';
import { ProfiloComponent } from './components/profilo/profilo.component';
import { OrdiniComponent } from './components/ordini/ordini.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'prodotti', component: ProdottiComponent },
  { path: 'prodotto/:id', component: ProdottoDettaglioComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'carrello', component: CarrelloComponent, canActivate: [authGuard] },
  { path: 'profilo', component: ProfiloComponent, canActivate: [authGuard] },
  { path: 'ordini', component: OrdiniComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '', pathMatch: 'full' } // Rotta di fallback
];
