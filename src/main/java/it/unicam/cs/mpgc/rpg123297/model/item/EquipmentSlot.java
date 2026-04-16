package it.unicam.cs.mpgc.rpg123297.model.item;

/**
 * Enum che definisce gli slot di equipaggiamento disponibili.
 * Ogni slot può contenere un solo oggetto alla volta.
 */
public enum EquipmentSlot {

    WEAPON("Arma"),
    SHIELD("Scudo"),
    HELMET("Elmo"),
    CHEST("Armatura Petto"),
    LEGS("Gambali"),
    ACCESSORY("Accessorio");

    private final String displayName;

    EquipmentSlot(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
