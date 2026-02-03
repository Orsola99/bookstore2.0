import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule, ActivatedRoute } from '@angular/router';
import { AuthService } from '../../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  username: string = '';
  password: string = '';
  error: string = '';
  loading: boolean = false;
  returnUrl: string = '/';

  constructor(
    private authService: AuthService, 
    private router: Router,
    private route: ActivatedRoute
  ) {}
  
  ngOnInit(): void {
    // Ottieni il returnUrl dai parametri di query, se presente
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    
    // Se l'utente è già loggato, reindirizzalo alla home
    this.authService.currentUser$.subscribe(user => {
      if (user) {
        this.router.navigate(['/']);
      }
    });
  }

  onSubmit(): void {
    this.loading = true;
    this.error = '';
    
    if (!this.username || !this.password) {
      this.error = 'Inserisci username e password';
      this.loading = false;
      return;
    }

    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate([this.returnUrl]);
      },
      error: (err) => {
        this.loading = false;
        this.error = err.error?.message || 'Errore durante il login. Verifica le credenziali.';
      }
    });
  }
}
