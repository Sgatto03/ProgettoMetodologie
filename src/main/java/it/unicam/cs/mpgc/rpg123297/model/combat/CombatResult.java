package it.unicam.cs.mpgc.rpg123297.model.combat;

/**
 * Rappresenta il risultato di un combattimento completato.
 * Contiene informazioni su chi ha vinto, le ricompense ottenute
 * e il log completo del combattimento.
 */
public class CombatResult {

    /**
     * Esito possibile del combattimento.
     */
    public enum Outcome {
        VICTORY("Vittoria"),
        DEFEAT("Sconfitta");

        private final String displayName;

        Outcome(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    private final Outcome outcome;
    private final int goldEarned;
    private final int xpEarned;
    private final int fameEarned;
    private final int turnsElapsed;
    private final CombatLog log;
    private boolean leveledUp;

    public CombatResult(Outcome outcome, int goldEarned, int xpEarned,
            int fameEarned, int turnsElapsed, CombatLog log) {
        this.outcome = outcome;
        this.goldEarned = goldEarned;
        this.xpEarned = xpEarned;
        this.fameEarned = fameEarned;
        this.turnsElapsed = turnsElapsed;
        this.log = log;
        this.leveledUp = false;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public int getGoldEarned() {
        return goldEarned;
    }

    public int getXpEarned() {
        return xpEarned;
    }

    public int getFameEarned() {
        return fameEarned;
    }

    public int getTurnsElapsed() {
        return turnsElapsed;
    }

    public CombatLog getLog() {
        return log;
    }

    public boolean isVictory() {
        return outcome == Outcome.VICTORY;
    }

    public boolean isLeveledUp() {
        return leveledUp;
    }

    public void setLeveledUp(boolean leveledUp) {
        this.leveledUp = leveledUp;
    }
}
