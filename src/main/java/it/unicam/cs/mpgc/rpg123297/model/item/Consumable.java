package it.unicam.cs.mpgc.rpg123297.model.item;

import it.unicam.cs.mpgc.rpg123297.model.character.GameCharacter;

/**
 * Rappresenta un oggetto consumabile (pozioni, elisir, ecc.).
 * Viene usato una volta e poi rimosso dall'inventario.
 */
public class Consumable extends AbstractItem {

    /**
     * Tipo di effetto del consumabile.
     */
    public enum EffectType {
        HEAL("Cura"),
        DAMAGE_BOOST("Potenziamento Danno"),
        DEFENSE_BOOST("Potenziamento Difesa");

        private final String displayName;

        EffectType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private final EffectType effectType;
    private final int effectValue;

    public Consumable(String id, String name, String description,
            int value, ItemRarity rarity,
            EffectType effectType, int effectValue) {
        super(id, name, description, value, rarity);
        this.effectType = effectType;
        this.effectValue = (int) (effectValue * rarity.getStatMultiplier());
    }

    public EffectType getEffectType() {
        return effectType;
    }

    public int getEffectValue() {
        return effectValue;
    }

    /**
     * Applica l'effetto del consumabile al personaggio specificato.
     * 
     * @param target il personaggio su cui applicare l'effetto
     * @return una descrizione testuale dell'effetto applicato
     */
    public String applyEffect(GameCharacter target) {
        return switch (effectType) {
            case HEAL -> {
                target.heal(effectValue);
                yield String.format("%s cura %d HP.", getName(), effectValue);
            }
            case DAMAGE_BOOST -> {
                target.getStats().increase(
                        it.unicam.cs.mpgc.rpg123297.model.character.CharacterStats.StatType.STRENGTH,
                        effectValue);
                yield String.format("%s aumenta la Forza di %d.", getName(), effectValue);
            }
            case DEFENSE_BOOST -> {
                target.getStats().increase(
                        it.unicam.cs.mpgc.rpg123297.model.character.CharacterStats.StatType.ENDURANCE,
                        effectValue);
                yield String.format("%s aumenta la Resistenza di %d.", getName(), effectValue);
            }
        };
    }

    @Override
    public String toString() {
        return String.format("[%s] %s — %s: +%d (Valore: %d oro)",
                getRarity().getDisplayName(), getName(),
                effectType.getDisplayName(), effectValue, getValue());
    }
}
