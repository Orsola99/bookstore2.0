package it.orsola.bookstore.controller;

import it.orsola.bookstore.model.Indirizzo;
import it.orsola.bookstore.model.Utente;
import it.orsola.bookstore.service.IndirizzoService;
import it.orsola.bookstore.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/indirizzi")
@CrossOrigin(origins = "http://localhost:4200")
public class IndirizzoController {

    private final IndirizzoService indirizzoService;
    private final UtenteService utenteService;

    @Autowired
    public IndirizzoController(IndirizzoService indirizzoService, UtenteService utenteService) {
        this.indirizzoService = indirizzoService;
        this.utenteService = utenteService;
    }

    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<Indirizzo>> getIndirizziByUtente(@PathVariable Long utenteId) {
        Optional<Utente> utenteOpt = utenteService.findById(utenteId);
        
        if (utenteOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Utente utente = utenteOpt.get();
        List<Indirizzo> indirizzi = indirizzoService.findByUtente(utente);
        
        return ResponseEntity.ok(indirizzi);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIndirizzoById(@PathVariable Long id) {
        return indirizzoService.findById(id)
                .map(indirizzo -> ResponseEntity.ok(Map.of("success", true, "indirizzo", indirizzo)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "Indirizzo non trovato")));
    }

    @PostMapping
    public ResponseEntity<Indirizzo> createIndirizzo(@RequestBody Map<String, Object> request) {
        Long utenteId = Long.valueOf(request.get("utenteId").toString());
        
        Optional<Utente> utenteOpt = utenteService.findById(utenteId);
        
        if (utenteOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Utente utente = utenteOpt.get();
        
        Indirizzo indirizzo = new Indirizzo();
        indirizzo.setUtente(utente);
        indirizzo.setVia((String) request.get("via"));
        indirizzo.setCap((String) request.get("cap"));
        indirizzo.setCitta((String) request.get("citta"));
        
        // Assicuriamoci che la provincia sia limitata a 2 caratteri
        String provincia = (String) request.get("provincia");
        if (provincia != null && provincia.length() > 2) {
            provincia = provincia.substring(0, 2);
        }
        indirizzo.setProvincia(provincia);
        
        indirizzo.setNazione((String) request.get("nazione"));
        indirizzo.setTipo(request.get("tipo") != null ? Integer.parseInt(request.get("tipo").toString()) : 0);
        indirizzo.setTelefono((String) request.get("telefono"));
        
        Indirizzo nuovoIndirizzo = indirizzoService.save(indirizzo);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovoIndirizzo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateIndirizzo(@PathVariable Long id, @RequestBody Indirizzo indirizzo) {
        return indirizzoService.findById(id)
                .map(existingIndirizzo -> {
                    indirizzo.setIdIndirizzo(id);
                    indirizzo.setUtente(existingIndirizzo.getUtente()); // Mantieni l'utente originale
                    
                    // Assicuriamoci che la provincia sia limitata a 2 caratteri
                    String provincia = indirizzo.getProvincia();
                    if (provincia != null && provincia.length() > 2) {
                        indirizzo.setProvincia(provincia.substring(0, 2));
                    }
                    
                    Indirizzo updatedIndirizzo = indirizzoService.update(indirizzo);
                    return ResponseEntity.ok(updatedIndirizzo);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteIndirizzo(@PathVariable Long id) {
        return indirizzoService.findById(id)
                .map(indirizzo -> {
                    indirizzoService.delete(id);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
