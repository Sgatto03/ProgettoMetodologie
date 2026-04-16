package it.unicam.cs.mpgc.rpg123297.model.item;

/**
 * Enum che definisce i livelli di rarità degli oggetti.
 * Ogni rarità ha un moltiplicatore che influenza le statistiche dell'oggetto.
 */
public enum ItemRarity {

    COMMON("Comune", 1.0, "#AAAAAA"),
    UNCOMMON("Non Comune", 1.3, "#1EFF00"),
    RARE("Raro", 1.6, "#0070DD"),
    EPIC("Epico", 2.0, "#A335EE"),
    LEGENDARY("Leggendario", 2.5, "#FF8000");

    private final String displayName;
    private final double statMultiplier;
    private final String colorHex;

    ItemRarity(String displayName, double statMultiplier, String colorHex) {
        this.displayName = displayName;
        this.statMultiplier = statMultiplier;
        this.colorHex = colorHex;
    }

    public String getDisplayName() {
        return displayName;
    }

    public double getStatMultiplier() {
        return statMultiplier;
    }

    public String getColorHex() {
        return colorHex;
    }
}
