package it.unicam.cs.mpgc.rpg123297.model.combat;

import it.unicam.cs.mpgc.rpg123297.model.character.AbstractCharacter;
import it.unicam.cs.mpgc.rpg123297.model.character.GameCharacter;

import java.util.Random;

/**
 * Azione di attacco standard. Calcola il danno in base alla forza
 * dell'attaccante e al suo equipaggiamento, con possibilità di critico
 * e schivata.
 */
public class AttackAction implements CombatAction {

    private static final Random RANDOM = new Random();

    @Override
    public int execute(GameCharacter attacker, GameCharacter defender, CombatLog log) {
        // Controlla se il difensore schiva
        double dodgeChance = defender.getStats().calculateDodgeChance();
        if (RANDOM.nextDouble() * 100 < dodgeChance) {
            log.addEntry(String.format("%s schiva l'attacco di %s!",
                    defender.getName(), attacker.getName()));
            return 0;
        }

        // Calcola danno base
        int baseDamage = attacker.calculateAttackDamage();

        // Controlla critico
        double critChance = attacker.getStats().calculateCritChance();
        boolean isCritical = RANDOM.nextDouble() * 100 < critChance;
        if (isCritical) {
            baseDamage = (int) (baseDamage * 1.5);
        }

        // Applica danno al difensore
        int actualDamage = defender.takeDamage(baseDamage);

        // Log
        if (isCritical) {
            log.addEntry(String.format("💥 CRITICO! %s infligge %d danni a %s!",
                    attacker.getName(), actualDamage, defender.getName()));
        } else {
            log.addEntry(String.format("⚔️ %s attacca %s infliggendo %d danni.",
                    attacker.getName(), defender.getName(), actualDamage));
        }

        // Reset dello stato di difesa dell'attaccante
        if (attacker instanceof AbstractCharacter ac) {
            ac.setDefending(false);
        }

        return actualDamage;
    }

    @Override
    public String getName() {
        return "Attacca";
    }

    @Override
    public String getDescription() {
        return "Infligge danno basato su Forza e arma equipaggiata.";
    }

    @Override
    public boolean isAvailable(GameCharacter character) {
        return true;
    }
}
