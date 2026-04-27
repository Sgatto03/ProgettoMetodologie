package it.unicam.cs.mpgc.rpg123297.event;

import java.util.Objects;

/**
 * Rappresenta un evento del gioco che viene inoltrato tramite il
 * {@link GameEventBus}.
 * Ogni evento ha un tipo e un payload opzionale con dati aggiuntivi.
 */
public class GameEvent {

    private final EventType type;
    private final Object data;

    /**
     * Crea un nuovo evento.
     * 
     * @param type il tipo di evento
     * @param data dati opzionali associati all'evento (può essere null)
     */
    public GameEvent(EventType type, Object data) {
        this.type = Objects.requireNonNull(type, "Il tipo di evento non può essere null");
        this.data = data;
    }

    /** Crea un evento senza dati aggiuntivi. */
    public GameEvent(EventType type) {
        this(type, null);
    }

    public EventType getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    /**
     * Restituisce il dato castato al tipo specificato.
     * 
     * @throws ClassCastException se il tipo non corrisponde
     */
    @SuppressWarnings("unchecked")
    public <T> T getDataAs(Class<T> clazz) {
        return (T) data;
    }

    @Override
    public String toString() {
        return String.format("GameEvent[%s, data=%s]", type, data);
    }
}
