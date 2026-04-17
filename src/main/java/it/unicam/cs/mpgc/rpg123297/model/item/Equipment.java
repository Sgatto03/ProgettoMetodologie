package it.unicam.cs.mpgc.rpg123297.model.item;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

/**
 * Gestisce gli oggetti equipaggiati dal personaggio.
 * Ogni slot può contenere un solo oggetto alla volta.
 * L'equipaggiamento contribuisce al calcolo di attacco e difesa.
 */
public class Equipment {

    private final Map<EquipmentSlot, Item> slots;

    public Equipment() {
        this.slots = new EnumMap<>(EquipmentSlot.class);
    }

    /**
     * Equipaggia un oggetto nello slot specificato.
     * Se lo slot è già occupato, restituisce l'oggetto precedente.
     *
     * @param slot lo slot in cui equipaggiare
     * @param item l'oggetto da equipaggiare
     * @return l'oggetto precedentemente nello slot, se presente
     */
    public Optional<Item> equip(EquipmentSlot slot, Item item) {
        Item previous = slots.put(slot, item);
        return Optional.ofNullable(previous);
    }

    /**
     * Rimuove l'oggetto dallo slot specificato.
     * 
     * @return l'oggetto rimosso, se presente
     */
    public Optional<Item> unequip(EquipmentSlot slot) {
        Item removed = slots.remove(slot);
        return Optional.ofNullable(removed);
    }

    /**
     * Restituisce l'oggetto equipaggiato nello slot specificato.
     */
    public Optional<Item> getItemInSlot(EquipmentSlot slot) {
        return Optional.ofNullable(slots.get(slot));
    }

    /**
     * Verifica se uno slot è occupato.
     */
    public boolean isSlotOccupied(EquipmentSlot slot) {
        return slots.containsKey(slot);
    }

    /**
     * Restituisce una vista immutabile di tutti gli oggetti equipaggiati.
     */
    public Map<EquipmentSlot, Item> getAllEquipped() {
        return Map.copyOf(slots);
    }

    /**
     * Calcola il bonus totale di attacco da tutto l'equipaggiamento.
     */
    public int getTotalAttackBonus() {
        int total = 0;
        for (Item item : slots.values()) {
            if (item instanceof Weapon weapon) {
                total += weapon.getDamage();
            }
        }
        return total;
    }

    /**
     * Calcola il bonus totale di difesa da tutto l'equipaggiamento.
     */
    public int getTotalDefenseBonus() {
        int total = 0;
        for (Item item : slots.values()) {
            if (item instanceof Armor armor) {
                total += armor.getDefense();
            }
        }
        return total;
    }

    /**
     * Rimuove tutto l'equipaggiamento.
     */
    public void clear() {
        slots.clear();
    }
}
