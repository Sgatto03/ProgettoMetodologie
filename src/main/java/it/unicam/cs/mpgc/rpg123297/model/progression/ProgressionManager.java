package it.unicam.cs.mpgc.rpg123297.model.progression;

import it.unicam.cs.mpgc.rpg123297.model.character.CharacterStats;
import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;

/**
 * Gestisce la progressione del gladiatore: livelli, statistiche e fama.
 * Fornisce le ricompense per il level-up e calcola il rango di fama corrente.
 */
public class ProgressionManager {

    /** Punti statistica bonus ottenuti per ogni level-up. */
    private static final int STAT_POINTS_PER_LEVEL = 2;

    /**
     * Calcola il rango di fama corrente del gladiatore.
     */
    public FameRank getCurrentRank(Gladiator gladiator) {
        return FameRank.fromFame(gladiator.getFame());
    }

    /**
     * Restituisce la fama necessaria per raggiungere il prossimo rango.
     * 
     * @return fama necessaria, o -1 se il rango massimo è già raggiunto
     */
    public int getFameToNextRank(Gladiator gladiator) {
        FameRank current = getCurrentRank(gladiator);
        FameRank next = current.getNextRank();
        if (next == null)
            return -1;
        return next.getFameRequired() - gladiator.getFame();
    }

    /**
     * Restituisce la percentuale di progresso verso il prossimo rango.
     * 
     * @return percentuale (0.0 - 1.0), o 1.0 se al rango massimo
     */
    public double getProgressToNextRank(Gladiator gladiator) {
        FameRank current = getCurrentRank(gladiator);
        FameRank next = current.getNextRank();
        if (next == null)
            return 1.0;

        int fameInCurrentRank = gladiator.getFame() - current.getFameRequired();
        int fameNeeded = next.getFameRequired() - current.getFameRequired();
        return Math.min(1.0, (double) fameInCurrentRank / fameNeeded);
    }

    /**
     * Applica un miglioramento di statistica al gladiatore.
     * Usato quando il giocatore sceglie quale stat aumentare al level-up.
     *
     * @param gladiator     il gladiatore da potenziare
     * @param statType      la statistica da aumentare
     * @param pointsToSpend quanti punti investire (max: STAT_POINTS_PER_LEVEL)
     */
    public void applyStatIncrease(Gladiator gladiator, CharacterStats.StatType statType,
            int pointsToSpend) {
        if (pointsToSpend < 0 || pointsToSpend > STAT_POINTS_PER_LEVEL) {
            throw new IllegalArgumentException(
                    "Punti da assegnare non validi: " + pointsToSpend);
        }
        gladiator.getStats().increase(statType, pointsToSpend);
    }

    /**
     * Restituisce il numero di punti statistica ottenuti per level-up.
     */
    public int getStatPointsPerLevel() {
        return STAT_POINTS_PER_LEVEL;
    }

    /**
     * Calcola la percentuale di progresso verso il prossimo livello.
     */
    public double getProgressToNextLevel(Gladiator gladiator) {
        return (double) gladiator.getExperience() / gladiator.getExperienceForNextLevel();
    }
}
