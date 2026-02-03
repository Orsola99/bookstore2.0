package it.orsola.bookstore.controller;

import it.orsola.bookstore.dto.CarrelloDTO;
import it.orsola.bookstore.dto.CarrelloDettaglioDTO;
import it.orsola.bookstore.model.Carrello;
import it.orsola.bookstore.model.CarrelloDettaglio;
import it.orsola.bookstore.model.Prodotto;
import it.orsola.bookstore.model.Utente;
import it.orsola.bookstore.service.CarrelloService;
import it.orsola.bookstore.service.ProdottoService;
import it.orsola.bookstore.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/carrello")
@CrossOrigin(origins = "http://localhost:4200")
public class CarrelloController {

    private final CarrelloService carrelloService;
    private final UtenteService utenteService;
    private final ProdottoService prodottoService;

    @Autowired
    public CarrelloController(CarrelloService carrelloService, UtenteService utenteService, ProdottoService prodottoService) {
        this.carrelloService = carrelloService;
        this.utenteService = utenteService;
        this.prodottoService = prodottoService;
    }

    @GetMapping("/{utenteId}")
    public ResponseEntity<?> getCarrello(@PathVariable Long utenteId) {
        Optional<Utente> utenteOpt = utenteService.findById(utenteId);
        
        if (utenteOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Utente non trovato"));
        }
        
        Utente utente = utenteOpt.get();
        Carrello carrello = carrelloService.getOrCreateCarrello(utente);
        BigDecimal totale = carrelloService.calcolaTotale(carrello);
        
        CarrelloDTO carrelloDTO = CarrelloDTO.fromEntity(carrello, totale);
        
        Map<String, Object> response = new HashMap<>();
        response.put("carrello", carrelloDTO);
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{utenteId}/aggiungi")
    public ResponseEntity<?> aggiungiAlCarrello(
            @PathVariable Long utenteId,
            @RequestBody Map<String, Object> request) {
        
        Long prodottoId = Long.valueOf(request.get("prodottoId").toString());
        BigDecimal quantita = new BigDecimal(request.get("quantita").toString());
        
        Optional<Utente> utenteOpt = utenteService.findById(utenteId);
        Optional<Prodotto> prodottoOpt = prodottoService.findById(prodottoId);
        
        if (utenteOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Utente non trovato"));
        }
        
        if (prodottoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Prodotto non trovato"));
        }
        
        Utente utente = utenteOpt.get();
        Prodotto prodotto = prodottoOpt.get();
        
        Carrello carrello = carrelloService.getOrCreateCarrello(utente);
        CarrelloDettaglio dettaglio = carrelloService.aggiungiProdotto(carrello, prodotto, quantita);
        
        BigDecimal totale = carrelloService.calcolaTotale(carrello);
        
        CarrelloDTO carrelloDTO = CarrelloDTO.fromEntity(carrello, totale);
        CarrelloDettaglioDTO dettaglioDTO = CarrelloDettaglioDTO.fromEntity(dettaglio);
        
        Map<String, Object> response = new HashMap<>();
        response.put("carrello", carrelloDTO);
        response.put("dettaglio", dettaglioDTO);
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{utenteId}/rimuovi/{prodottoId}")
    public ResponseEntity<?> rimuoviDalCarrello(
            @PathVariable Long utenteId,
            @PathVariable Long prodottoId) {
        
        Optional<Utente> utenteOpt = utenteService.findById(utenteId);
        Optional<Prodotto> prodottoOpt = prodottoService.findById(prodottoId);
        
        if (utenteOpt.isEmpty() || prodottoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Utente o prodotto non trovato"));
        }
        
        Utente utente = utenteOpt.get();
        Prodotto prodotto = prodottoOpt.get();
        
        Optional<Carrello> carrelloOpt = carrelloService.findByUtente(utente);
        
        if (carrelloOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Carrello non trovato"));
        }
        
        Carrello carrello = carrelloOpt.get();
        carrelloService.rimuoviProdotto(carrello, prodotto);
        
        BigDecimal totale = carrelloService.calcolaTotale(carrello);
        
        CarrelloDTO carrelloDTO = CarrelloDTO.fromEntity(carrello, totale);
        
        Map<String, Object> response = new HashMap<>();
        response.put("carrello", carrelloDTO);
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{utenteId}/aggiorna")
    public ResponseEntity<?> aggiornaQuantita(
            @PathVariable Long utenteId,
            @RequestBody Map<String, Object> request) {
        
        Long dettaglioId = Long.valueOf(request.get("dettaglioId").toString());
        BigDecimal quantita = new BigDecimal(request.get("quantita").toString());
        
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
        List<CarrelloDettaglio> dettagli = carrello.getDettagli();
        
        Optional<CarrelloDettaglio> dettaglioOpt = dettagli.stream()
                .filter(d -> d.getIdCarrelloDettagli().equals(dettaglioId))
                .findFirst();
        
        if (dettaglioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Dettaglio non trovato"));
        }
        
        CarrelloDettaglio dettaglio = dettaglioOpt.get();
        
        carrelloService.aggiornaQuantita(dettaglio, quantita);
        
        BigDecimal totale = carrelloService.calcolaTotale(carrello);
        
        CarrelloDTO carrelloDTO = CarrelloDTO.fromEntity(carrello, totale);
        
        Map<String, Object> response = new HashMap<>();
        response.put("carrello", carrelloDTO);
        response.put("success", true);
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{utenteId}/svuota")
    public ResponseEntity<?> svuotaCarrello(@PathVariable Long utenteId) {
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
        carrelloService.svuotaCarrello(carrello);
        
        return ResponseEntity.ok(Map.of("success", true, "message", "Carrello svuotato con successo"));
    }
}
