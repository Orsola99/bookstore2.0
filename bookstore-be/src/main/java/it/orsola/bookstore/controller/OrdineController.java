package it.orsola.bookstore.controller;

import it.orsola.bookstore.model.Carrello;
import it.orsola.bookstore.model.Ordine;
import it.orsola.bookstore.model.Utente;
import it.orsola.bookstore.service.CarrelloService;
import it.orsola.bookstore.service.IndirizzoService;
import it.orsola.bookstore.service.OrdineService;
import it.orsola.bookstore.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordini")
@CrossOrigin(origins = "http://localhost:4200")
public class OrdineController {

    private final OrdineService ordineService;
    private final UtenteService utenteService;
    private final CarrelloService carrelloService;
    private final IndirizzoService indirizzoService;

    @Autowired
    public OrdineController(OrdineService ordineService, UtenteService utenteService, 
                           CarrelloService carrelloService, IndirizzoService indirizzoService) {
        this.ordineService = ordineService;
        this.utenteService = utenteService;
        this.carrelloService = carrelloService;
        this.indirizzoService = indirizzoService;
    }

    @GetMapping
    public ResponseEntity<List<Ordine>> getAllOrdini() {
        return ResponseEntity.ok(ordineService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ordine> getOrdineById(@PathVariable Long id) {
        return ordineService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<?> getOrdiniByUtente(@PathVariable Long utenteId) {
        Optional<Utente> utenteOpt = utenteService.findById(utenteId);
        
        if (utenteOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Utente non trovato"));
        }
        
        Utente utente = utenteOpt.get();
        List<Ordine> ordini = ordineService.findByUtente(utente);
        
        return ResponseEntity.ok(Map.of("success", true, "ordini", ordini));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Ordine>> getOrdiniByStatus(@PathVariable int status) {
        return ResponseEntity.ok(ordineService.findByStatus(status));
    }
    
    @GetMapping("/data")
    public ResponseEntity<List<Ordine>> getOrdiniByData(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date data) {
        return ResponseEntity.ok(ordineService.findByDataOrdineAfter(data));
    }
    
    @GetMapping("/data-status")
    public ResponseEntity<List<Ordine>> getOrdiniByDataAndStatus(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date data,
            @RequestParam int status) {
        return ResponseEntity.ok(ordineService.findByDataOrdineAfterAndStatus(data, status));
    }

    @PostMapping("/crea")
    public ResponseEntity<?> creaOrdine(@RequestBody Map<String, Object> request) {
        Long utenteId = Long.valueOf(request.get("utenteId").toString());
        Long indirizzoSpedizioneId = Long.valueOf(request.get("indirizzoSpedizioneId").toString());
        Long indirizzoFatturazioneId = Long.valueOf(request.get("indirizzoFatturazioneId").toString());

        Optional<Utente> utenteOpt = utenteService.findById(utenteId);
        
        if (utenteOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Utente non trovato"));
        }
        
        Utente utente = utenteOpt.get();
        
        Optional<Carrello> carrelloOpt = carrelloService.findByUtente(utente);
        
        if (carrelloOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Carrello non trovato"));
        }
        
        Carrello carrello = carrelloOpt.get();
        
        if (carrello.getDettagli() == null || carrello.getDettagli().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Il carrello è vuoto"));
        }
        
        try {
            Ordine ordine = ordineService.creaOrdine(utente, carrello, indirizzoSpedizioneId, indirizzoFatturazioneId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("success", true, "ordine", ordine));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Errore durante la creazione dell'ordine: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<?> aggiornaStatoOrdine(@PathVariable Long id, @PathVariable int status) {
        try {
            Ordine ordine = ordineService.aggiornaStatoOrdine(id, status);
            return ResponseEntity.ok(Map.of("success", true, "ordine", ordine));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "Errore durante l'aggiornamento dello stato dell'ordine: " + e.getMessage()));
        }
    }
}
