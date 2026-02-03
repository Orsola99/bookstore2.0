package it.orsola.bookstore.controller;

import it.orsola.bookstore.model.Prodotto;
import it.orsola.bookstore.service.OrdineDettaglioService;
import it.orsola.bookstore.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prodotti")
@CrossOrigin(origins = "http://localhost:4200")
public class ProdottoController {

    private final ProdottoService prodottoService;
    private final OrdineDettaglioService ordineDettaglioService;

    @Autowired
    public ProdottoController(ProdottoService prodottoService, OrdineDettaglioService ordineDettaglioService) {
        this.prodottoService = prodottoService;
        this.ordineDettaglioService = ordineDettaglioService;
    }

    @GetMapping
    public ResponseEntity<List<Prodotto>> getAllProdotti() {
        return ResponseEntity.ok(prodottoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prodotto> getProdottoById(@PathVariable Long id) {
        return prodottoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search/titolo")
    public ResponseEntity<List<Prodotto>> searchByTitolo(@RequestParam String titolo) {
        return ResponseEntity.ok(prodottoService.findByTitolo(titolo));
    }

    @GetMapping("/search/sottotitolo")
    public ResponseEntity<List<Prodotto>> searchBySottotitolo(@RequestParam String sottotitolo) {
        return ResponseEntity.ok(prodottoService.findBySottotitolo(sottotitolo));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Prodotto>> search(@RequestParam String query) {
        return ResponseEntity.ok(prodottoService.search(query));
    }

    @GetMapping("/search/prezzo")
    public ResponseEntity<List<Prodotto>> searchByPrezzo(
            @RequestParam BigDecimal minPrezzo,
            @RequestParam BigDecimal maxPrezzo) {
        return ResponseEntity.ok(prodottoService.findByPrezzoBetween(minPrezzo, maxPrezzo));
    }

    @GetMapping("/{id}/vendite")
    public ResponseEntity<?> getVendite(@PathVariable Long id) {
        return prodottoService.findById(id)
                .map(prodotto -> {
                    BigDecimal quantitaVenduta = ordineDettaglioService.getTotalQuantitaVendutaByProdottoId(id);
                    Map<String, Object> response = new HashMap<>();
                    response.put("prodotto", prodotto);
                    response.put("quantitaVenduta", quantitaVenduta);
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Prodotto> createProdotto(@RequestBody Prodotto prodotto) {
        Prodotto nuovoProdotto = prodottoService.save(prodotto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovoProdotto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Prodotto> updateProdotto(@PathVariable Long id, @RequestBody Prodotto prodotto) {
        return prodottoService.findById(id)
                .map(existingProdotto -> {
                    prodotto.setIdProdotto(id);
                    return ResponseEntity.ok(prodottoService.update(prodotto));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteProdotto(@PathVariable Long id) {
        return prodottoService.findById(id)
                .map(prodotto -> {
                    prodottoService.delete(id);
                    return ResponseEntity.ok(Map.of("deleted", true));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
