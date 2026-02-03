import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  loading = false;
  submitted = false;
  error = '';
  success = '';

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthService
  ) {
    // Reindirizza alla home se già loggato
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
    }

    this.registerForm = this.formBuilder.group({
      nome: ['', Validators.required],
      cognome: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      telefono: ['']
    });
  }

  ngOnInit() {
  }

  // Getter per un accesso facile ai campi del form
  get f() { return this.registerForm.controls; }

  onSubmit() {
    this.submitted = true;

    // Stop se il form non è valido
    if (this.registerForm.invalid) {
      return;
    }

    this.loading = true;
    this.authService.register({
      idUtente: 0,
      nome: this.f['nome'].value,
      cognome: this.f['cognome'].value,
      email: this.f['email'].value,
      username: this.f['username'].value,
      password: this.f['password'].value,
      telefono: this.f['telefono'].value,
      attivo: true
    })
    .subscribe({
      next: () => {
        this.success = 'Registrazione completata con successo! Ora puoi accedere.';
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 3000);
      },
      error: error => {
        this.error = error.error?.message || 'Errore durante la registrazione. Riprova più tardi.';
        this.loading = false;
      }
    });
  }
}
