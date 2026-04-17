package it.unicam.cs.mpgc.rpg123297.model.item;

/**
 * Rappresenta un'arma equipaggiabile dal gladiatore.
 * Ogni arma ha un valore di danno base e uno slot di equipaggiamento.
 */
public class Weapon extends AbstractItem {

    private final int damage;
    private final EquipmentSlot slot; // può essere WEAPON o SHIELD (off-hand)

    public Weapon(String id, String name, String description,
            int value, ItemRarity rarity, int damage, EquipmentSlot slot) {
        super(id, name, description, value, rarity);
        this.damage = (int) (damage * rarity.getStatMultiplier());
        this.slot = slot;
    }

    /** Crea un'arma per lo slot WEAPON. */
    public Weapon(String id, String name, String description,
            int value, ItemRarity rarity, int damage) {
        this(id, name, description, value, rarity, damage, EquipmentSlot.WEAPON);
    }

    /** Restituisce il danno dell'arma (già moltiplicato per la rarità). */
    public int getDamage() {
        return damage;
    }

    /** Restituisce lo slot in cui questa arma va equipaggiata. */
    public EquipmentSlot getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s — Danno: %d (Valore: %d oro)",
                getRarity().getDisplayName(), getName(), damage, getValue());
    }
}
