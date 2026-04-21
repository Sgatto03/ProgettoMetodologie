package it.unicam.cs.mpgc.rpg123297.model.character;

import it.unicam.cs.mpgc.rpg123297.model.item.Inventory;

import java.util.Objects;

/**
 * Rappresenta il gladiatore controllato dal giocatore.
 * Gestisce la classe del gladiatore, l'inventario, l'oro, il livello,
 * l'esperienza e la fama accumulata.
 */
public class Gladiator extends AbstractCharacter {

    private final GladiatorClass gladiatorClass;
    private final Inventory inventory;

    private int level;
    private int experience;
    private int gold;
    private int fame;
    private int unspentStatPoints; // punti statistica da assegnare (2 per level-up)
    private int specialCooldown; // turni rimanenti prima di poter riusare l'abilità speciale

    /**
     * Crea un nuovo gladiatore con la classe specificata.
     * 
     * @param name           nome del gladiatore
     * @param gladiatorClass classe scelta dal giocatore
     */
    public Gladiator(String name, GladiatorClass gladiatorClass) {
        super(name, gladiatorClass.getBaseStats());
        this.gladiatorClass = Objects.requireNonNull(gladiatorClass);
        this.inventory = new Inventory();
        this.level = 1;
        this.experience = 0;
        this.gold = 50; // oro iniziale
        this.fame = 0;
        this.unspentStatPoints = 0;
        this.specialCooldown = 0;
    }

    public GladiatorClass getGladiatorClass() {
        return gladiatorClass;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getGold() {
        return gold;
    }

    public int getFame() {
        return fame;
    }

    public int getUnspentStatPoints() {
        return unspentStatPoints;
    }

    public int getSpecialCooldown() {
        return specialCooldown;
    }

    /** Calcola l'XP necessaria per il prossimo livello. Formula: livello * 100 */
    public int getExperienceForNextLevel() {
        return level * 100;
    }

    /**
     * Aggiunge esperienza e gestisce il level-up automatico.
     * 
     * @return true se il gladiatore è salito di livello
     */
    public boolean addExperience(int xp) {
        if (xp < 0)
            throw new IllegalArgumentException("L'XP non può essere negativa");
        this.experience += xp;
        boolean leveledUp = false;
        while (experience >= getExperienceForNextLevel()) {
            experience -= getExperienceForNextLevel();
            level++;
            unspentStatPoints += 2; // 2 punti statistica per ogni level-up
            leveledUp = true;
        }
        return leveledUp;
    }

    /** Aggiunge oro al gladiatore. */
    public void addGold(int amount) {
        this.gold += amount;
    }

    /**
     * Spende oro. Restituisce true se la transazione è riuscita.
     * 
     * @return true se l'oro era sufficiente e la spesa è stata effettuata
     */
    public boolean spendGold(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("L'importo non può essere negativo");
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }

    /** Aggiunge fama al gladiatore, applicando il moltiplicatore del Carisma. */
    public void addFame(int baseFame) {
        double multiplier = getStats().calculateFameMultiplier();
        this.fame += (int) (baseFame * multiplier);
    }

    /**
     * Verifica se l'abilità speciale è pronta.
     */
    public boolean isSpecialReady() {
        return specialCooldown <= 0;
    }

    /**
     * Attiva l'abilità speciale, impostando il cooldown a 3 turni.
     */
    public void useSpecial() {
        if (!isSpecialReady()) {
            throw new IllegalStateException("L'abilità speciale è ancora in cooldown");
        }
        this.specialCooldown = 3;
    }

    /**
     * Riduce il cooldown dell'abilità speciale di 1 turno.
     */
    public void tickCooldown() {
        if (specialCooldown > 0) {
            specialCooldown--;
        }
    }

    @Override
    public void resetForCombat() {
        super.resetForCombat();
        this.specialCooldown = 0;
    }

    /** Imposta il livello (usato per il caricamento dei salvataggi). */
    public void setLevel(int level) {
        this.level = level;
    }

    /** Imposta l'esperienza (usato per il caricamento dei salvataggi). */
    public void setExperience(int experience) {
        this.experience = experience;
    }

    /** Imposta l'oro (usato per il caricamento dei salvataggi). */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /** Imposta la fama (usato per il caricamento dei salvataggi). */
    public void setFame(int fame) {
        this.fame = fame;
    }

    /**
     * Imposta i punti statistica non spesi (usato per il caricamento dei
     * salvataggi).
     */
    public void setUnspentStatPoints(int points) {
        this.unspentStatPoints = points;
    }

    /**
     * Spende un punto statistica nella statistica indicata.
     * 
     * @param statType la statistica da aumentare
     * @throws IllegalStateException se non ci sono punti disponibili
     */
    public void spendStatPoint(CharacterStats.StatType statType) {
        if (unspentStatPoints <= 0) {
            throw new IllegalStateException("Nessun punto statistica disponibile");
        }
        getStats().increase(statType, 1);
        unspentStatPoints--;
    }
}
