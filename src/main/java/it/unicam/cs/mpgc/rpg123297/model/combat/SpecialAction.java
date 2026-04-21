package it.unicam.cs.mpgc.rpg123297.model.combat;

import it.unicam.cs.mpgc.rpg123297.model.character.GameCharacter;
import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;
import it.unicam.cs.mpgc.rpg123297.model.character.GladiatorClass;

/**
 * Azione speciale unica per ogni classe di gladiatore.
 * L'effetto dipende dalla {@link GladiatorClass} del gladiatore.
 * Ha un cooldown di 3 turni dopo l'uso.
 */
public class SpecialAction implements CombatAction {

    @Override
    public int execute(GameCharacter attacker, GameCharacter defender, CombatLog log) {
        if (!(attacker instanceof Gladiator gladiator)) {
            log.addEntry("Solo i gladiatori possono usare abilità speciali!");
            return 0;
        }

        gladiator.useSpecial();
        GladiatorClass gc = gladiator.getGladiatorClass();

        return switch (gc) {
            case MURMILLO -> executeShieldBash(gladiator, defender, log);
            case RETIARIUS -> executeNetTrap(gladiator, defender, log);
            case THRAEX -> executeFrenzy(gladiator, defender, log);
            case SECUTOR -> executeFortress(gladiator, defender, log);
        };
    }

    /** Colpo di Scudo: danno medio + stordisce il nemico per 1 turno. */
    private int executeShieldBash(Gladiator attacker, GameCharacter defender, CombatLog log) {
        int damage = (int) (attacker.calculateAttackDamage() * 0.8);
        int actual = defender.takeDamage(damage);
        // Lo stordimento è gestito dal CombatEngine (il nemico salta il prossimo turno)
        log.addEntry(String.format("🔰 %s usa Colpo di Scudo! Infligge %d danni e stordisce %s!",
                attacker.getName(), actual, defender.getName()));
        return actual;
    }

    /** Trappola di Rete: riduce l'agilità del nemico. */
    private int executeNetTrap(Gladiator attacker, GameCharacter defender, CombatLog log) {
        int agiReduction = 3;
        defender.getStats().increase(
                it.unicam.cs.mpgc.rpg123297.model.character.CharacterStats.StatType.AGILITY,
                -agiReduction);
        log.addEntry(String.format("🕸️ %s lancia la rete! L'agilità di %s è ridotta di %d!",
                attacker.getName(), defender.getName(), agiReduction));
        return 0;
    }

    /** Frenesia: attacca due volte. */
    private int executeFrenzy(Gladiator attacker, GameCharacter defender, CombatLog log) {
        log.addEntry(String.format("⚡ %s entra in Frenesia! Doppio attacco!", attacker.getName()));
        AttackAction attack = new AttackAction();
        int total = attack.execute(attacker, defender, log);
        if (defender.isAlive()) {
            total += attack.execute(attacker, defender, log);
        }
        return total;
    }

    /** Fortezza: blocca tutto il danno per il turno corrente. */
    private int executeFortress(Gladiator attacker, GameCharacter defender, CombatLog log) {
        // Nella cura come compensazione: 15% HP
        int healAmount = (int) (attacker.getMaxHp() * 0.15);
        attacker.heal(healAmount);
        // Il blocco danno è gestito tramite setDefending(true) con moltiplicatore extra
        if (attacker instanceof it.unicam.cs.mpgc.rpg123297.model.character.AbstractCharacter ac) {
            ac.setDefending(true);
        }
        log.addEntry(String.format("🏰 %s attiva Fortezza! Blocca i danni e recupera %d HP!",
                attacker.getName(), healAmount));
        return 0;
    }

    @Override
    public String getName() {
        return "Speciale";
    }

    @Override
    public String getDescription() {
        return "Usa l'abilità speciale della classe (cooldown: 3 turni).";
    }

    @Override
    public boolean isAvailable(GameCharacter character) {
        if (character instanceof Gladiator gladiator) {
            return gladiator.isSpecialReady();
        }
        return false;
    }
}
