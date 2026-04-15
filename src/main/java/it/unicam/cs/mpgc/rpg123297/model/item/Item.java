package it.unicam.cs.mpgc.rpg123297.model.item;

/**
 * Contratto base per tutti gli oggetti del gioco.
 * Ogni oggetto ha un nome, una descrizione, un valore in oro.
 */
public interface Item {

    /** Restituisce l'identificativo univoco dell'oggetto. */
    String getId();

    /** Restituisce il nome dell'oggetto. */
    String getName();

    /** Restituisce la descrizione dell'oggetto. */
    String getDescription();

    /** Restituisce il valore in oro dell'oggetto. */
    int getValue();

}