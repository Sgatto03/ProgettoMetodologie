package it.unicam.cs.mpgc.rpg123297.model.combat;

import it.unicam.cs.mpgc.rpg123297.model.character.AbstractCharacter;
import it.unicam.cs.mpgc.rpg123297.model.character.GameCharacter;

/**
 * Azione di difesa. Attiva la modalità difensiva che dimezza il danno
 * in entrata per il turno corrente e cura una piccola quantità di HP.
 */
public class DefendAction implements CombatAction {

    private static final double HEAL_PERCENTAGE = 0.05; // 5% degli HP massimi

    @Override
    public int execute(GameCharacter attacker, GameCharacter defender, CombatLog log) {
        if (attacker instanceof AbstractCharacter ac) {
            ac.setDefending(true);
        }

        // Piccola cura
        int healAmount = (int) (attacker.getMaxHp() * HEAL_PERCENTAGE);
        attacker.heal(healAmount);

        log.addEntry(String.format("🛡️ %s si mette in difesa e recupera %d HP.",
                attacker.getName(), healAmount));

        return 0;
    }

    @Override
    public String getName() {
        return "Difendi";
    }

    @Override
    public String getDescription() {
        return "Dimezza il danno in entrata e cura il 5% degli HP.";
    }

    @Override
    public boolean isAvailable(GameCharacter character) {
        return true;
    }
}
