package it.unicam.cs.mpgc.rpg123297.persistence;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia Repository per la persistenza dei dati di gioco.
 * Astrae il meccanismo di salvataggio/caricamento, permettendo
 * di sostituire l'implementazione (JSON, SQLite, cloud) senza
 * modificare il resto del codice (DIP, OCP).
 */
public interface GameRepository {

    /**
     * Salva lo stato corrente del gioco.
     * 
     * @param saveData i dati da salvare
     * @param slotName nome dello slot di salvataggio
     * @return true se il salvataggio è riuscito
     */
    boolean save(SaveData saveData, String slotName);

    /**
     * Carica lo stato del gioco da uno slot specifico.
     * 
     * @param slotName nome dello slot da caricare
     * @return i dati caricati, o Optional.empty() se non trovato
     */
    Optional<SaveData> load(String slotName);

    /**
     * Elimina un salvataggio.
     * 
     * @param slotName nome dello slot da eliminare
     * @return true se il salvataggio è stato eliminato
     */
    boolean delete(String slotName);

    /**
     * Restituisce la lista degli slot di salvataggio disponibili.
     * 
     * @return lista di nomi degli slot
     */
    List<String> listSaves();

    /**
     * Verifica se uno slot di salvataggio esiste.
     */
    boolean exists(String slotName);
}
