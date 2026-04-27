package it.unicam.cs.mpgc.rpg123297.event;

/**
 * Enum che definisce i tipi di eventi del gioco.
 * Il sistema di eventi disaccoppia il model dalla view (Observer pattern).
 */
public enum EventType {
    // --- Navigazione ---
    STATE_CHANGED,

    // --- Personaggio ---
    GLADIATOR_CREATED,
    GLADIATOR_LEVEL_UP,
    GLADIATOR_FAME_CHANGED,

    // --- Combattimento ---
    COMBAT_STARTED,
    COMBAT_ACTION_PERFORMED,
    COMBAT_ENDED,

    // --- Negozio ---
    ITEM_PURCHASED,
    ITEM_SOLD,

    // --- Equipaggiamento ---
    ITEM_EQUIPPED,
    ITEM_UNEQUIPPED,

    // --- Persistenza ---
    GAME_SAVED,
    GAME_LOADED,

    // --- Generico ---
    MESSAGE

    /*
     * --- Da aggiungere eventi casuali fuori dall'arena ---
     * --- Da basare su percentuali legate alle statistiche del gladiatore ---
     * --- Da basare su scelte tra due opzioni ---
     */
}
