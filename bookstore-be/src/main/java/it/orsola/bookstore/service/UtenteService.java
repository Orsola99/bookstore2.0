package it.orsola.bookstore.service;

import it.orsola.bookstore.model.Utente;
import it.orsola.bookstore.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;

    @Autowired
    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public List<Utente> findAll() {
        return utenteRepository.findAll();
    }

    public Optional<Utente> findById(Long id) {
        return utenteRepository.findById(id);
    }

    public Optional<Utente> findByUsername(String username) {
        return utenteRepository.findByUsername(username);
    }

    public Optional<Utente> login(String username, String password) {
        return utenteRepository.findByUsernameAndUserPass(username, password);
    }

    public boolean existsByUsername(String username) {
        return utenteRepository.existsByUsername(username);
    }

    public boolean existsByMail(String mail) {
        return utenteRepository.existsByMail(mail);
    }

    @Transactional
    public Utente save(Utente utente) {
        return utenteRepository.save(utente);
    }

    @Transactional
    public void delete(Long id) {
        utenteRepository.deleteById(id);
    }

    @Transactional
    public Utente update(Utente utente) {
        return utenteRepository.save(utente);
    }
}
