package it.unicam.cs.mpgc.rpg123297.model.character;

/**
 * Contratto base per tutti i personaggi del gioco (giocatore e nemici).
 * Definisce le operazioni fondamentali che ogni personaggio deve supportare.
 */
public interface GameCharacter {

    /** Restituisce il nome del personaggio. */
    String getName();

    /** Restituisce i punti vita correnti. */
    int getCurrentHp();

    /** Restituisce i punti vita massimi. */
    int getMaxHp();

    /** Verifica se il personaggio è ancora in vita. */
    boolean isAlive();

    /**
     * Infligge danno al personaggio.
     * 
     * @param amount quantità di danno da infliggere (prima della riduzione da
     *               armatura)
     * @return il danno effettivamente subito
     */
    int takeDamage(int amount);

    /**
     * Cura il personaggio di una certa quantità di HP.
     * 
     * @param amount quantità di HP da ripristinare
     */
    void heal(int amount);

    /**
     * Calcola il danno di attacco totale (statistiche + equipaggiamento).
     * 
     * @return il valore di danno base
     */
    int calculateAttackDamage();

    /**
     * Calcola la difesa totale (statistiche + equipaggiamento).
     * 
     * @return il valore di difesa totale
     */
    int calculateDefense();

    /**
     * Ripristina il personaggio allo stato iniziale (HP pieno, effetti rimossi).
     */
    void resetForCombat();
}
