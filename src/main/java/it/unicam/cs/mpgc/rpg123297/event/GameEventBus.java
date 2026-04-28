package it.unicam.cs.mpgc.rpg123297.event;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Bus di eventi centralizzato (Observer/Publish-Subscribe pattern).
 * Permette ai componenti di pubblicare e sottoscrivere eventi
 * senza dipendere direttamente l'uno dall'altro.
 * <p>
 * Singleton: un'unica istanza condivisa da tutta l'applicazione.
 */
public class GameEventBus {

    private static GameEventBus instance;

    private final Map<EventType, List<Consumer<GameEvent>>> listeners;

    private GameEventBus() {
        this.listeners = new EnumMap<>(EventType.class);
    }

    /** Restituisce l'istanza singleton dell'event bus. */
    public static synchronized GameEventBus getInstance() {
        if (instance == null) {
            instance = new GameEventBus();
        }
        return instance;
    }

    /**
     * Registra un listener per un tipo di evento specifico.
     * 
     * @param type     il tipo di evento da ascoltare
     * @param listener il consumer che verrà invocato quando l'evento viene
     *                 pubblicato
     */
    public void subscribe(EventType type, Consumer<GameEvent> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    /**
     * Rimuove un listener per un tipo di evento specifico.
     */
    public void unsubscribe(EventType type, Consumer<GameEvent> listener) {
        List<Consumer<GameEvent>> list = listeners.get(type);
        if (list != null) {
            list.remove(listener);
        }
    }

    /**
     * Pubblica un evento, notificando tutti i listener registrati.
     * 
     * @param event l'evento da pubblicare
     */
    public void publish(GameEvent event) {
        List<Consumer<GameEvent>> list = listeners.get(event.getType());
        if (list != null) {
            // Copia la lista per evitare ConcurrentModificationException
            new ArrayList<>(list).forEach(listener -> listener.accept(event));
        }
    }

    /**
     * Pubblica un evento con tipo e dati.
     */
    public void publish(EventType type, Object data) {
        publish(new GameEvent(type, data));
    }

    /**
     * Pubblica un evento senza dati.
     */
    public void publish(EventType type) {
        publish(new GameEvent(type));
    }

    /**
     * Rimuove tutti i listener registrati (utile per il reset).
     */
    public void clearAll() {
        listeners.clear();
    }

    /**
     * Resetta l'istanza singleton (usato per testing).
     */
    public static synchronized void resetInstance() {
        instance = null;
    }
}
