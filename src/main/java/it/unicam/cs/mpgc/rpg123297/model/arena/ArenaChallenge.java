package it.unicam.cs.mpgc.rpg123297.model.arena;

import it.unicam.cs.mpgc.rpg123297.model.character.Enemy;
import it.unicam.cs.mpgc.rpg123297.model.character.EnemyFactory;

import java.util.Objects;

/**
 * Rappresenta una sfida specifica all'interno di un'arena.
 * Ogni sfida genera un nemico con statistiche adeguate al tier.
 */
public class ArenaChallenge {

    private final String name;
    private final String description;
    private final int tier;

    /**
     * Crea una nuova sfida.
     * 
     * @param name        nome della sfida
     * @param description descrizione della sfida
     * @param tier        difficoltà della sfida (1-5)
     */
    public ArenaChallenge(String name, String description, int tier) {
        this.name = Objects.requireNonNull(name);
        this.description = Objects.requireNonNull(description);
        this.tier = tier;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getTier() {
        return tier;
    }

    /**
     * Genera un nuovo nemico per questa sfida.
     * Ogni invocazione crea un nemico diverso grazie alla casualità della factory.
     */
    public Enemy generateEnemy() {
        return EnemyFactory.createRandomEnemy(tier);
    }
}
