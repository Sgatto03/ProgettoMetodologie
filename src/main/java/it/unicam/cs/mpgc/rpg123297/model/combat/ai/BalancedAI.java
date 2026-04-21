package it.unicam.cs.mpgc.rpg123297.model.combat.ai;

import it.unicam.cs.mpgc.rpg123297.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg123297.model.combat.AttackAction;
import it.unicam.cs.mpgc.rpg123297.model.combat.CombatAction;
import it.unicam.cs.mpgc.rpg123297.model.combat.DefendAction;

import java.util.Random;

/**
 * Strategia IA bilanciata: sceglie l'azione in base alla situazione tattica.
 * Considera la propria salute e quella dell'avversario per ottimizzare le
 * scelte.
 */
public class BalancedAI implements EnemyAI {

    private static final AttackAction ATTACK = new AttackAction();
    private static final DefendAction DEFEND = new DefendAction();
    private static final Random RANDOM = new Random();

    @Override
    public CombatAction chooseAction(GameCharacter self, GameCharacter opponent) {
        double selfHpPercent = (double) self.getCurrentHp() / self.getMaxHp();
        double opponentHpPercent = (double) opponent.getCurrentHp() / opponent.getMaxHp();

        // Se l'avversario sta per morire, attacca per finirlo
        if (opponentHpPercent < 0.2) {
            return ATTACK;
        }

        // Se ha pochi HP, difenditi per recuperare
        if (selfHpPercent < 0.3) {
            return DEFEND;
        }

        // Se ha HP medi, mix casuale con preferenza per l'attacco
        if (selfHpPercent < 0.6) {
            return RANDOM.nextDouble() < 0.4 ? DEFEND : ATTACK;
        }

        // HP alti: attacco con probabilità alta
        return RANDOM.nextDouble() < 0.15 ? DEFEND : ATTACK;
    }

    @Override
    public String getStrategyName() {
        return "Bilanciata";
    }
}
