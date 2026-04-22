package it.unicam.cs.mpgc.rpg123297.model.combat;

import it.unicam.cs.mpgc.rpg123297.model.character.Enemy;
import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;

import java.util.Objects;

/**
 * Motore del combattimento a turni. Gestisce il flusso del combattimento
 * e applica le azioni scelte dal giocatore e dall'IA nemica.
 * <p>
 * Il flusso è strutturato come un Template Method:
 * <ol>
 * <li>Inizializzazione combattimento</li>
 * <li>Turno del giocatore (scelta azione)</li>
 * <li>Turno del nemico (azione scelta dall'IA)</li>
 * <li>Controllo condizioni di fine</li>
 * <li>Ripetizione o conclusione</li>
 * </ol>
 */
public class CombatEngine {

    private final Gladiator gladiator;
    private final Enemy enemy;
    private final CombatLog log;

    private int turnCount;
    private boolean combatOver;
    private boolean playerTurn;
    private boolean enemyStunned; // stordimento da Colpo di Scudo
    private CombatResult result;

    /**
     * Crea un nuovo motore di combattimento.
     * 
     * @param gladiator il gladiatore del giocatore
     * @param enemy     il nemico da affrontare
     */
    public CombatEngine(Gladiator gladiator, Enemy enemy) {
        this.gladiator = Objects.requireNonNull(gladiator);
        this.enemy = Objects.requireNonNull(enemy);
        this.log = new CombatLog();
        this.turnCount = 0;
        this.combatOver = false;
        this.playerTurn = true;
        this.enemyStunned = false;
    }

    /**
     * Inizializza il combattimento, preparando entrambi i combattenti.
     */
    public void initCombat() {
        gladiator.resetForCombat();
        enemy.resetForCombat();
        log.clear();
        turnCount = 0;
        combatOver = false;
        playerTurn = determineInitiative();

        log.addEntry(String.format("⚔️ === COMBATTIMENTO: %s vs %s === ⚔️",
                gladiator.getName(), enemy.getName()));

        if (playerTurn) {
            log.addEntry(String.format("%s ha l'iniziativa!", gladiator.getName()));
        } else {
            log.addEntry(String.format("%s ha l'iniziativa!", enemy.getName()));
        }
    }

    /**
     * Esegue il turno del giocatore con l'azione scelta.
     * 
     * @param action l'azione scelta dal giocatore
     */
    public void executePlayerTurn(CombatAction action) {
        if (combatOver || !playerTurn)
            return;

        turnCount++;
        gladiator.tickCooldown();

        log.addEntry(String.format("\n--- Turno %d: %s ---", turnCount, gladiator.getName()));
        action.execute(gladiator, enemy, log);

        // Controlla se l'azione speciale è "Colpo di Scudo" (stordimento)
        if (action instanceof SpecialAction
                && gladiator
                        .getGladiatorClass() == it.unicam.cs.mpgc.rpg123297.model.character.GladiatorClass.MURMILLO) {
            enemyStunned = true;
        }

        checkCombatEnd();
        if (!combatOver) {
            playerTurn = false;
        }
    }

    /**
     * Esegue il turno del nemico usando la sua strategia IA.
     */
    public void executeEnemyTurn() {
        if (combatOver || playerTurn)
            return;

        log.addEntry(String.format("\n--- Turno %d: %s ---", turnCount, enemy.getName()));

        if (enemyStunned) {
            log.addEntry(String.format("💫 %s è stordito e non può agire!", enemy.getName()));
            enemyStunned = false;
        } else {
            CombatAction enemyAction = enemy.getAiStrategy().chooseAction(enemy, gladiator);
            enemyAction.execute(enemy, gladiator, log);
        }

        checkCombatEnd();
        if (!combatOver) {
            playerTurn = true;
        }
    }

    /**
     * Controlla se il combattimento è terminato.
     */
    private void checkCombatEnd() {
        if (!gladiator.isAlive()) {
            combatOver = true;
            log.addEntry(String.format("\n☠️ %s è stato sconfitto!", gladiator.getName()));
            result = new CombatResult(CombatResult.Outcome.DEFEAT,
                    0, 0, 0, turnCount, log);
        } else if (!enemy.isAlive()) {
            combatOver = true;
            log.addEntry(String.format("\n🏆 %s ha vinto!", gladiator.getName()));

            // Calcola ricompense con bonus carisma
            int gold = enemy.getGoldReward();
            int xp = enemy.getXpReward();
            int fame = enemy.getFameReward();

            // Applica ricompense al gladiatore
            gladiator.addGold(gold);
            boolean leveledUp = gladiator.addExperience(xp);
            gladiator.addFame(fame);

            result = new CombatResult(CombatResult.Outcome.VICTORY,
                    gold, xp, fame, turnCount, log);
            result.setLeveledUp(leveledUp);

            log.addEntry(String.format("💰 Ricompense: %d oro, %d XP, %d fama",
                    gold, xp, fame));
            if (leveledUp) {
                log.addEntry(String.format("⬆️ %s è salito al livello %d!",
                        gladiator.getName(), gladiator.getLevel()));
            }
        }
    }

    /**
     * Determina chi attacca per primo in base all'agilità.
     * 
     * @return true se il giocatore ha l'iniziativa
     */
    private boolean determineInitiative() {
        return gladiator.getStats().getAgility() >= enemy.getStats().getAgility();
    }

    // --- Getters ---

    public Gladiator getGladiator() {
        return gladiator;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public CombatLog getLog() {
        return log;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public boolean isCombatOver() {
        return combatOver;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public CombatResult getResult() {
        return result;
    }
}
