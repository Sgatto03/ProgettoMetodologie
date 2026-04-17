package it.unicam.cs.mpgc.rpg123297.model.character;

import it.unicam.cs.mpgc.rpg123297.model.item.Equipment;

import java.util.Objects;

/**
 * Implementazione astratta di {@link GameCharacter} che fornisce
 * la logica comune a tutti i personaggi (gestione HP, danno, difesa).
 */
public abstract class AbstractCharacter implements GameCharacter {

    private final String name;
    private final CharacterStats stats;
    private final Equipment equipment;

    private int currentHp;
    private boolean defending;
    private int tempAttackBoost; // buff temporaneo attacco (solo per il combattimento corrente)
    private int tempDefenseBoost; // buff temporaneo difesa (solo per il combattimento corrente)

    /**
     * Crea un nuovo personaggio con nome e statistiche date.
     */
    protected AbstractCharacter(String name, CharacterStats stats) {
        this.name = Objects.requireNonNull(name, "Il nome non può essere null");
        this.stats = Objects.requireNonNull(stats, "Le statistiche non possono essere null");
        this.equipment = new Equipment();
        this.currentHp = stats.calculateMaxHp();
        this.defending = false;
        this.tempAttackBoost = 0;
        this.tempDefenseBoost = 0;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public CharacterStats getStats() {
        return stats;
    }

    @Override
    public int getCurrentHp() {
        return currentHp;
    }

    @Override
    public int getMaxHp() {
        return stats.calculateMaxHp();
    }

    @Override
    public Equipment getEquipment() {
        return equipment;
    }

    @Override
    public boolean isAlive() {
        return currentHp > 0;
    }

    public boolean isDefending() {
        return defending;
    }

    public void setDefending(boolean defending) {
        this.defending = defending;
    }

    @Override
    public int takeDamage(int amount) {
        int defense = calculateDefense();
        int effectiveDamage = Math.max(1, amount - defense);

        if (defending) {
            effectiveDamage = Math.max(1, effectiveDamage / 2);
        }

        currentHp = Math.max(0, currentHp - effectiveDamage);
        return effectiveDamage;
    }

    @Override
    public void heal(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("La quantità di cura non può essere negativa");
        }
        currentHp = Math.min(getMaxHp(), currentHp + amount);
    }

    @Override
    public int calculateAttackDamage() {
        int baseDamage = stats.getStrength() * 2;
        int weaponDamage = equipment.getTotalAttackBonus();
        return baseDamage + weaponDamage + tempAttackBoost;
    }

    @Override
    public int calculateDefense() {
        return stats.getEndurance() + equipment.getTotalDefenseBonus() + tempDefenseBoost;
    }

    @Override
    public void resetForCombat() {
        this.currentHp = getMaxHp();
        this.defending = false;
        this.tempAttackBoost = 0;
        this.tempDefenseBoost = 0;
    }

    /**
     * Applica un buff temporaneo all'attacco (dura solo il combattimento corrente).
     */
    public void addTempAttackBoost(int amount) {
        this.tempAttackBoost += amount;
    }

    /**
     * Applica un buff temporaneo alla difesa (dura solo il combattimento corrente).
     */
    public void addTempDefenseBoost(int amount) {
        this.tempDefenseBoost += amount;
    }

    /**
     * Imposta direttamente gli HP correnti (usato per il caricamento dei
     * salvataggi).
     */
    protected void setCurrentHp(int hp) {
        this.currentHp = Math.max(0, Math.min(getMaxHp(), hp));
    }

    @Override
    public String toString() {
        return String.format("%s [HP: %d/%d, %s]", name, currentHp, getMaxHp(), stats);
    }
}
