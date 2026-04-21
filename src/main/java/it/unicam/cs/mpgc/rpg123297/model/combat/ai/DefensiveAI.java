package it.unicam.cs.mpgc.rpg123297.model.combat.ai;

import it.unicam.cs.mpgc.rpg123297.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg123297.model.combat.AttackAction;
import it.unicam.cs.mpgc.rpg123297.model.combat.CombatAction;
import it.unicam.cs.mpgc.rpg123297.model.combat.DefendAction;

/**
 * Strategia IA difensiva: alterna difesa e attacco.
 * Si difende quando ha meno del 50% degli HP, attacca altrimenti.
 */
public class DefensiveAI implements EnemyAI {

    private static final AttackAction ATTACK = new AttackAction();
    private static final DefendAction DEFEND = new DefendAction();

    @Override
    public CombatAction chooseAction(GameCharacter self, GameCharacter opponent) {
        double hpPercentage = (double) self.getCurrentHp() / self.getMaxHp();

        if (hpPercentage < 0.5) {
            return DEFEND;
        }
        return ATTACK;
    }

    @Override
    public String getStrategyName() {
        return "Difensiva";
    }
}
