package it.unicam.cs.mpgc.rpg123297.model.item;

/**
 * Rappresenta un pezzo di armatura equipaggiabile dal gladiatore.
 * Ogni armatura ha un valore di difesa e va in uno specifico slot.
 */
public class Armor extends AbstractItem {

    private final int defense;
    private final EquipmentSlot slot;

    public Armor(String id, String name, String description,
            int value, ItemRarity rarity, int defense, EquipmentSlot slot) {
        super(id, name, description, value, rarity);
        this.defense = (int) (defense * rarity.getStatMultiplier());
        this.slot = slot;
    }

    /** Restituisce la difesa dell'armatura (già moltiplicata per la rarità). */
    public int getDefense() {
        return defense;
    }

    /** Restituisce lo slot in cui questa armatura va equipaggiata. */
    public EquipmentSlot getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s — Difesa: %d (Valore: %d oro)",
                getRarity().getDisplayName(), getName(), defense, getValue());
    }
}
