package it.unicam.cs.mpgc.rpg123297.model.item;

import java.util.Objects;

/**
 * Implementazione astratta di {@link Item} che fornisce i campi comuni
 * a tutti gli oggetti (id, nome, descrizione, valore, rarità).
 */
public abstract class AbstractItem implements Item {

    private final String id;
    private final String name;
    private final String description;
    private final int value;
    private final ItemRarity rarity;

    protected AbstractItem(String id, String name, String description,
            int value, ItemRarity rarity) {
        this.id = Objects.requireNonNull(id);
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.value = value;
        this.rarity = Objects.requireNonNull(rarity);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public ItemRarity getRarity() {
        return rarity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AbstractItem that))
            return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s - %s (Valore: %d oro)",
                rarity.getDisplayName(), name, description, value);
    }
}
