import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { Utente } from '../../../models/utente.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  utente: Utente = {
    nome: '',
    cognome: '',
    mail: '',
    username: '',
    userPass: '',
    privacy: 1 // Assumiamo che l'utente accetti la privacy policy
  };
  
  // Campo per la conferma della password
  confirmPassword: string = '';
  error: string = '';
  loading: boolean = false;
  success: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  onSubmit(): void {
    this.loading = true;
    this.error = '';
    this.success = false;
    
    // Validazione base
    if (!this.utente.nome || !this.utente.cognome || !this.utente.mail || 
        !this.utente.username || !this.utente.userPass) {
      this.error = 'Tutti i campi sono obbligatori';
      this.loading = false;
      return;
    }

    // Verifica che le password coincidano
    if (this.utente.userPass !== this.confirmPassword) {
      this.error = 'Le password non coincidono';
      this.loading = false;
      return;
    }

    // Validazione email
    const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
    if (!emailPattern.test(this.utente.mail)) {
      this.error = 'Inserisci un indirizzo email valido';
      this.loading = false;
      return;
    }

    this.authService.register(this.utente).subscribe({
      next: () => {
        this.loading = false;
        this.success = true;
        // Reindirizza al login dopo 2 secondi
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Errore durante la registrazione. Riprova più tardi.';
      }
    });
  }
}
