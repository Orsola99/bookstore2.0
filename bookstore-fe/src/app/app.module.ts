import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';

// Componenti
import { HeaderComponent } from './components/shared/header/header.component';
import { FooterComponent } from './components/shared/footer/footer.component';
import { HomeComponent } from './components/home/home.component';

// Servizi
import { AuthService } from './services/auth.service';
import { ProdottoService } from './services/prodotto.service';
import { CarrelloService } from './services/carrello.service';
import { OrdineService } from './services/ordine.service';

const routes: Routes = [
  { path: '', component: HomeComponent },
  // Rotta di fallback
  { path: '**', redirectTo: '/' }
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule.forRoot(routes)
  ],
  providers: [
    AuthService,
    ProdottoService,
    CarrelloService,
    OrdineService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
