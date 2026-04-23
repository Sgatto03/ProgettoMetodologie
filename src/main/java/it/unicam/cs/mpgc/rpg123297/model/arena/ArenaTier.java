package it.unicam.cs.mpgc.rpg123297.model.arena;

/**
 * Enum che definisce i livelli (tier) delle arene.
 * Ogni tier ha requisiti di fama crescenti e ricompense maggiori.
 */
public enum ArenaTier {

    STREET(1, "Fossa dei Reietti",
            "Un lurido buco nel terreno dove combattono disperati e criminali.",
            0),
    PROVINCIAL(2, "Arena di Provincia",
            "Un'arena di provincia dove si allenano i giovani gladiatori.",
            100),
    IMPERIAL(3, "Arena Imperiale",
            "L'arena della città imperiale, teatro di scontri epici.",
            300),
    COLOSSEUM(4, "Il Colosseo",
            "Il più grande anfiteatro dell'Impero, tempio del combattimento.",
            600),
    DIVINE(5, "Arena degli Dei",
            "Un'arena leggendaria dove solo i più grandi possono combattere.",
            1000);

    private final int tier;
    private final String displayName;
    private final String description;
    private final int fameRequired;

    ArenaTier(int tier, String displayName, String description, int fameRequired) {
        this.tier = tier;
        this.displayName = displayName;
        this.description = description;
        this.fameRequired = fameRequired;
    }

    public int getTier() {
        return tier;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getFameRequired() {
        return fameRequired;
    }
}
