package it.unicam.cs.mpgc.rpg123297.model.combat.ai;

import it.unicam.cs.mpgc.rpg123297.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg123297.model.combat.CombatAction;

/**
 * Interfaccia Strategy per il comportamento dell'IA nemica.
 * Ogni implementazione rappresenta una diversa strategia di combattimento.
 * Nuove strategie possono essere aggiunte senza modificare il codice esistente
 * (OCP).
 */
public interface EnemyAI {

    /**
     * Sceglie l'azione da eseguire per il nemico in base alla situazione attuale.
     *
     * @param self     il nemico che deve agire
     * @param opponent il gladiatore avversario
     * @return l'azione di combattimento scelta
     */
    CombatAction chooseAction(GameCharacter self, GameCharacter opponent);

    /** Restituisce il nome della strategia IA. */
    String getStrategyName();
}
