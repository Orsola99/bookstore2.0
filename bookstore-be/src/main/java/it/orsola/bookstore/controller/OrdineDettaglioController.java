package it.orsola.bookstore.controller;

import it.orsola.bookstore.model.Ordine;
import it.orsola.bookstore.model.OrdineDettaglio;
import it.orsola.bookstore.model.Prodotto;
import it.orsola.bookstore.service.OrdineDettaglioService;
import it.orsola.bookstore.service.OrdineService;
import it.orsola.bookstore.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ordine-dettagli")
@CrossOrigin(origins = "http://localhost:4200")
public class OrdineDettaglioController {

    private final OrdineDettaglioService ordineDettaglioService;
    private final OrdineService ordineService;
    private final ProdottoService prodottoService;

    @Autowired
    public OrdineDettaglioController(OrdineDettaglioService ordineDettaglioService, 
                                    OrdineService ordineService,
                                    ProdottoService prodottoService) {
        this.ordineDettaglioService = ordineDettaglioService;
        this.ordineService = ordineService;
        this.prodottoService = prodottoService;
    }

    @GetMapping
    public ResponseEntity<List<OrdineDettaglio>> getAllOrdineDettagli() {
        return ResponseEntity.ok(ordineDettaglioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdineDettaglio> getOrdineDettaglioById(@PathVariable Long id) {
        return ordineDettaglioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ordine/{ordineId}")
    public ResponseEntity<?> getDettagliByOrdine(@PathVariable Long ordineId) {
        Optional<Ordine> ordineOpt = ordineService.findById(ordineId);
        
        if (ordineOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Ordine non trovato"));
        }
        
        Ordine ordine = ordineOpt.get();
        List<OrdineDettaglio> dettagli = ordineDettaglioService.findByOrdine(ordine);
        
        return ResponseEntity.ok(Map.of("success", true, "dettagli", dettagli));
    }

    @GetMapping("/prodotto/{prodottoId}")
    public ResponseEntity<?> getDettagliByProdotto(@PathVariable Long prodottoId) {
        Optional<Prodotto> prodottoOpt = prodottoService.findById(prodottoId);
        
        if (prodottoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Prodotto non trovato"));
        }
        
        Prodotto prodotto = prodottoOpt.get();
        List<OrdineDettaglio> dettagli = ordineDettaglioService.findByProdotto(prodotto);
        
        return ResponseEntity.ok(Map.of("success", true, "dettagli", dettagli));
    }
    
    @GetMapping("/prodotto/{prodottoId}/vendite")
    public ResponseEntity<?> getQuantitaVendutaByProdotto(@PathVariable Long prodottoId) {
        Optional<Prodotto> prodottoOpt = prodottoService.findById(prodottoId);
        
        if (prodottoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Prodotto non trovato"));
        }
        
        BigDecimal quantitaVenduta = ordineDettaglioService.getTotalQuantitaVendutaByProdottoId(prodottoId);
        
        return ResponseEntity.ok(Map.of("success", true, "prodotto", prodottoOpt.get(), "quantitaVenduta", quantitaVenduta));
    }

    @PostMapping
    public ResponseEntity<OrdineDettaglio> createOrdineDettaglio(@RequestBody OrdineDettaglio ordineDettaglio) {
        OrdineDettaglio nuovoDettaglio = ordineDettaglioService.save(ordineDettaglio);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovoDettaglio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrdineDettaglio(@PathVariable Long id, @RequestBody OrdineDettaglio ordineDettaglio) {
        return ordineDettaglioService.findById(id)
                .map(existingDettaglio -> {
                    ordineDettaglio.setIdDettaglio(id);
                    return ResponseEntity.ok(ordineDettaglioService.save(ordineDettaglio));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrdineDettaglio(@PathVariable Long id) {
        return ordineDettaglioService.findById(id)
                .map(dettaglio -> {
                    ordineDettaglioService.delete(id);
                    return ResponseEntity.ok(Map.of("success", true, "message", "Dettaglio ordine eliminato con successo"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
