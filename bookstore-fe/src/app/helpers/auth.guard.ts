import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable()
export class AuthGuard implements CanActivate {
    constructor(
        private router: Router,
        private authService: AuthService
    ) { }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        if (this.authService.isLoggedIn()) {
            // Utente loggato, permetti l'accesso
            return true;
        }

        // Utente non loggato, reindirizza alla pagina di login con l'URL di ritorno
        this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
        return false;
    }
}
