package it.unicam.cs.mpgc.rpg123297.model.progression;

/**
 * Enum che definisce i ranghi di fama del gladiatore.
 * Ogni rango viene raggiunto accumulando una certa quantità di fama.
 */
public enum FameRank {

    NOVIZIO("Novizio", 0,
            "Un gladiatore appena entrato nell'arena."),
    COMBATTENTE("Combattente", 100,
            "Un gladiatore che ha dimostrato di saper combattere."),
    CAMPIONE("Campione", 300,
            "Un gladiatore rispettato e temuto."),
    LEGGENDA("Leggenda", 600,
            "Un gladiatore il cui nome risuona in tutto l'Impero."),
    DIO_ARENA("Dio dell'Arena", 1000,
            "Il gladiatore più grande che sia mai esistito.");

    private final String displayName;
    private final int fameRequired;
    private final String description;

    FameRank(String displayName, int fameRequired, String description) {
        this.displayName = displayName;
        this.fameRequired = fameRequired;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getFameRequired() {
        return fameRequired;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Restituisce il rango di fama corrispondente alla fama data.
     * 
     * @param fame la fama corrente del giocatore
     * @return il rango più alto raggiunto
     */
    public static FameRank fromFame(int fame) {
        FameRank result = NOVIZIO;
        for (FameRank rank : values()) {
            if (fame >= rank.fameRequired) {
                result = rank;
            }
        }
        return result;
    }

    /**
     * Restituisce il rango successivo, o null se è il massimo.
     */
    public FameRank getNextRank() {
        FameRank[] values = values();
        int nextOrdinal = this.ordinal() + 1;
        return nextOrdinal < values.length ? values[nextOrdinal] : null;
    }
}
