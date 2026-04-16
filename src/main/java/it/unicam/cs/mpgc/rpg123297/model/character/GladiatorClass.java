package it.unicam.cs.mpgc.rpg123297.model.character;

/**
 * Enum che definisce le classi di gladiatore disponibili nel gioco.
 * Ogni classe ha statistiche base diverse e un'abilità speciale unica.
 */
public enum GladiatorClass {

    MURMILLO("Murmillo",
            "Combattente pesante con spada e scudo. Eccelle in forza e resistenza.",
            new CharacterStats(8, 4, 7, 4, 2),
            "Colpo di Scudo",
            "Stordisce il nemico per 1 turno, impedendogli di agire."),

    RETIARIUS("Retiarius",
            "Combattente agile con tridente e rete. Eccelle in agilità e destrezza.",
            new CharacterStats(4, 8, 3, 7, 3),
            "Trappola di Rete",
            "Riduce l'agilità del nemico per 3 turni."),

    THRAEX("Thraex",
            "Combattente bilanciato con spada curva e piccolo scudo.",
            new CharacterStats(6, 6, 5, 5, 3),
            "Frenesia",
            "Attacca due volte nello stesso turno."),

    SECUTOR("Secutor",
            "Tank pesante con armatura massima. Eccelle in resistenza.",
            new CharacterStats(6, 3, 9, 3, 4),
            "Fortezza",
            "Blocca completamente il danno per 1 turno.");

    private final String displayName;
    private final String description;
    private final CharacterStats baseStats;
    private final String specialAbilityName;
    private final String specialAbilityDescription;

    GladiatorClass(String displayName, String description, CharacterStats baseStats,
            String specialAbilityName, String specialAbilityDescription) {
        this.displayName = displayName;
        this.description = description;
        this.baseStats = baseStats;
        this.specialAbilityName = specialAbilityName;
        this.specialAbilityDescription = specialAbilityDescription;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /** Restituisce una copia delle statistiche base per evitare mutazioni. */
    public CharacterStats getBaseStats() {
        return new CharacterStats(baseStats);
    }

    public String getSpecialAbilityName() {
        return specialAbilityName;
    }

    public String getSpecialAbilityDescription() {
        return specialAbilityDescription;
    }
}
