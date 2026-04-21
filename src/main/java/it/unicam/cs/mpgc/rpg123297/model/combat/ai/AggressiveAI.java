package it.unicam.cs.mpgc.rpg123297.model.combat.ai;

import it.unicam.cs.mpgc.rpg123297.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg123297.model.combat.AttackAction;
import it.unicam.cs.mpgc.rpg123297.model.combat.CombatAction;

/**
 * Strategia IA aggressiva: attacca sempre senza difendersi.
 * Efficace quando il nemico ha statistiche offensive elevate.
 */
public class AggressiveAI implements EnemyAI {

    private static final AttackAction ATTACK = new AttackAction();

    @Override
    public CombatAction chooseAction(GameCharacter self, GameCharacter opponent) {
        return ATTACK;
    }

    @Override
    public String getStrategyName() {
        return "Aggressiva";
    }
}
