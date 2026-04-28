package it.unicam.cs.mpgc.rpg123297.view;

import javafx.scene.Parent;

/**
 * Interfaccia base per tutte le viste del gioco.
 * Astrae la rappresentazione grafica per permettere implementazioni
 * su piattaforme diverse (desktop JavaFX, Android, web).
 */
public interface GameView {

    /**
     * Restituisce il nodo radice della vista (specifico per JavaFX).
     * Per implementazioni non-JavaFX, questo metodo andrà adattato.
     */
    Parent getRoot();

    /**
     * Aggiorna il contenuto della vista con i dati correnti.
     */
    void refresh();

    /**
     * Restituisce il titolo della vista.
     */
    String getTitle();
}
