package it.orsola.bookstore.service;

import it.orsola.bookstore.model.Indirizzo;
import it.orsola.bookstore.model.Utente;
import it.orsola.bookstore.repository.IndirizzoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class IndirizzoService {

    private final IndirizzoRepository indirizzoRepository;

    @Autowired
    public IndirizzoService(IndirizzoRepository indirizzoRepository) {
        this.indirizzoRepository = indirizzoRepository;
    }

    public List<Indirizzo> findByUtente(Utente utente) {
        return indirizzoRepository.findByUtente(utente);
    }

    public Optional<Indirizzo> findById(Long id) {
        return indirizzoRepository.findById(id);
    }

    @Transactional
    public Indirizzo save(Indirizzo indirizzo) {
        return indirizzoRepository.save(indirizzo);
    }

    @Transactional
    public void delete(Long id) {
        indirizzoRepository.deleteById(id);
    }

    @Transactional
    public Indirizzo update(Indirizzo indirizzo) {
        return indirizzoRepository.save(indirizzo);
    }
}
