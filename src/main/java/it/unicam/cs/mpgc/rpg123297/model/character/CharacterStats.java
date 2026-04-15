package it.unicam.cs.mpgc.rpg123297.model.character;

import java.util.Objects;

/**
 * Value object che incapsula le statistiche di un personaggio.
 * Le statistiche determinano le capacità in combattimento e le interazioni nel
 * gioco.
 */
public class CharacterStats {

    private int strength; // Forza: danno fisico base
    private int agility; // Agilità: schivata, iniziativa
    private int endurance; // Resistenza: HP massimi, stamina
    private int dexterity; // Destrezza: critico, precisione
    private int charisma; // Carisma: bonus oro, fama, sconti

    /**
     * Crea un nuovo insieme di statistiche.
     */
    public CharacterStats(int strength, int agility, int endurance,
            int dexterity, int charisma) {
        this.strength = strength;
        this.agility = agility;
        this.endurance = endurance;
        this.dexterity = dexterity;
        this.charisma = charisma;
    }

    /** Crea una copia delle statistiche. */
    public CharacterStats(CharacterStats other) {
        this(other.strength, other.agility, other.endurance,
                other.dexterity, other.charisma);
    }

    // --- Getters ---

    public int getStrength() {
        return strength;
    }

    public int getAgility() {
        return agility;
    }

    public int getEndurance() {
        return endurance;
    }

    public int getDexterity() {
        return dexterity;
    }

    public int getCharisma() {
        return charisma;
    }

    // --- Setters ---

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    /**
     * Calcola i punti vita massimi in base alla Resistenza.
     * Formula: 50 + (endurance * 10)
     */
    public int calculateMaxHp() {
        return 50 + (endurance * 10);
    }

    /**
     * Calcola la probabilità di critico in base alla Destrezza (%).
     * Formula: 5 + (dexterity * 2), cap a 50%
     */
    public double calculateCritChance() {
        return Math.min(5.0 + (dexterity * 2.0), 50.0);
    }

    /**
     * Calcola la probabilità di schivata in base all'Agilità (%).
     * Formula: 3 + (agility * 1.5), cap a 40%
     */
    public double calculateDodgeChance() {
        return Math.min(3.0 + (agility * 1.5), 40.0);
    }

    /**
     * Calcola il moltiplicatore di fama in base al Carisma.
     * Formula: 1.0 + (charisma * 0.05)
     */
    public double calculateFameMultiplier() {
        return 1.0 + (charisma * 0.05);
    }

    /**
     * Calcola lo sconto al negozio in base al Carisma (%).
     * Formula: charisma * 2, cap a 30%
     */
    public double calculateShopDiscount() {
        return Math.min(charisma * 2.0, 30.0);
    }

    /**
     * Incrementa una singola statistica di un valore dato.
     */
    public void increase(StatType type, int amount) {
        Objects.requireNonNull(type, "Il tipo di statistica non può essere null");
        switch (type) {
            case STRENGTH -> strength += amount;
            case AGILITY -> agility += amount;
            case ENDURANCE -> endurance += amount;
            case DEXTERITY -> dexterity += amount;
            case CHARISMA -> charisma += amount;
        }
    }

    /**
     * Enum per identificare i tipi di statistica.
     */
    public enum StatType {
        STRENGTH("Forza"),
        AGILITY("Agilità"),
        ENDURANCE("Resistenza"),
        DEXTERITY("Destrezza"),
        CHARISMA("Carisma");

        private final String displayName;

        StatType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Override
    public String toString() {
        return String.format("STR:%d AGI:%d END:%d DEX:%d CHA:%d",
                strength, agility, endurance, dexterity, charisma);
    }
}
