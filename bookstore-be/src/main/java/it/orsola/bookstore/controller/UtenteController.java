package it.orsola.bookstore.controller;

import it.orsola.bookstore.model.Utente;
import it.orsola.bookstore.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/utenti")
@CrossOrigin(origins = "http://localhost:4200")
public class UtenteController {

    private final UtenteService utenteService;

    @Autowired
    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping
    public ResponseEntity<List<Utente>> getAllUtenti() {
        return ResponseEntity.ok(utenteService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utente> getUtenteById(@PathVariable Long id) {
        return utenteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        return utenteService.login(username, password)
                .map(utente -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("utente", utente);
                    response.put("success", true);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "Username o password non validi")));
    }

    @PostMapping("/registrazione")
    public ResponseEntity<?> registrazione(@RequestBody Utente utente) {
        // Verifica se username o email sono già in uso
        if (utenteService.existsByUsername(utente.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Username già in uso"));
        }

        if (utenteService.existsByMail(utente.getMail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Email già in uso"));
        }

        Utente nuovoUtente = utenteService.save(utente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("success", true, "utente", nuovoUtente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUtente(@PathVariable Long id, @RequestBody Utente utente) {
        Optional<Utente> existingUtente = utenteService.findById(id);
        
        if (existingUtente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!existingUtente.get().getMail().equals(utente.getMail()) && 
            utenteService.existsByMail(utente.getMail())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Email già in uso"));
        }
        
        utente.setIdUtente(id);
        Utente updatedUtente = utenteService.update(utente);
        return ResponseEntity.ok(Map.of("success", true, "utente", updatedUtente));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<?> cambiaPassword(@PathVariable Long id, @RequestBody Map<String, String> passwords) {
        String vecchiaPassword = passwords.get("vecchiaPassword");
        String nuovaPassword = passwords.get("nuovaPassword");
        
        Optional<Utente> utenteOpt = utenteService.findById(id);
        
        if (utenteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Utente utente = utenteOpt.get();
        
        // Verifica la vecchia password
        if (!utente.getUserPass().equals(vecchiaPassword)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "Password attuale non corretta"));
        }
        
        utente.setUserPass(nuovaPassword);
        utenteService.update(utente);
        
        return ResponseEntity.ok(Map.of("success", true, "message", "Password aggiornata con successo"));
    }
}
