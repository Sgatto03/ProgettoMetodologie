package it.unicam.cs.mpgc.rpg123297.model.character;

import it.unicam.cs.mpgc.rpg123297.model.combat.ai.EnemyAI;

import java.util.Objects;

/**
 * Rappresenta un nemico che il gladiatore deve affrontare nell'arena.
 * Ogni nemico ha una strategia IA che determina il suo comportamento in
 * combattimento.
 */
public class Enemy extends AbstractCharacter {

    private final int tier; // livello di difficoltà del nemico
    private final String description;
    private final int goldReward; // oro ottenuto sconfiggendo il nemico
    private final int xpReward; // esperienza ottenuta sconfiggendolo
    private final int fameReward; // fama ottenuta sconfiggendolo
    private EnemyAI aiStrategy; // strategia IA (Strategy pattern)

    /**
     * Crea un nuovo nemico.
     * 
     * @param name        nome del nemico
     * @param stats       statistiche del nemico
     * @param tier        livello di difficoltà
     * @param description descrizione del nemico
     * @param goldReward  oro ottenuto alla sconfitta
     * @param xpReward    XP ottenuta alla sconfitta
     * @param fameReward  fama ottenuta alla sconfitta
     * @param aiStrategy  strategia IA per il combattimento
     */
    public Enemy(String name, CharacterStats stats, int tier,
            String description, int goldReward, int xpReward,
            int fameReward, EnemyAI aiStrategy) {
        super(name, stats);
        this.tier = tier;
        this.description = Objects.requireNonNull(description);
        this.goldReward = goldReward;
        this.xpReward = xpReward;
        this.fameReward = fameReward;
        this.aiStrategy = Objects.requireNonNull(aiStrategy);
    }

    public int getTier() {
        return tier;
    }

    public String getDescription() {
        return description;
    }

    public int getGoldReward() {
        return goldReward;
    }

    public int getXpReward() {
        return xpReward;
    }

    public int getFameReward() {
        return fameReward;
    }

    public EnemyAI getAiStrategy() {
        return aiStrategy;
    }

    /** Permette di cambiare la strategia IA a runtime (Strategy pattern). */
    public void setAiStrategy(EnemyAI aiStrategy) {
        this.aiStrategy = Objects.requireNonNull(aiStrategy);
    }
}
