import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './components/shared/header/header.component';
import { FooterComponent } from './components/shared/footer/footer.component';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
  imports: [RouterOutlet, HeaderComponent, FooterComponent],
  standalone: true
})
export class AppComponent implements OnInit {
  title = 'BookStore';

  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  ngOnInit(): void {
    // Verifica se l'applicazione è in esecuzione nel browser
    if (isPlatformBrowser(this.platformId)) {
      // Carica Bootstrap e Bootstrap Icons tramite CDN solo nel browser
      this.loadStylesheet('https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css');
      this.loadStylesheet('https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css');
      this.loadScript('https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js');
    }
  }

  private loadStylesheet(url: string): void {
    if (typeof document !== 'undefined') {
      const link = document.createElement('link');
      link.rel = 'stylesheet';
      link.href = url;
      document.head.appendChild(link);
    }
  }

  private loadScript(url: string): void {
    if (typeof document !== 'undefined') {
      const script = document.createElement('script');
      script.src = url;
      script.defer = true;
      document.body.appendChild(script);
    }
  }
}
