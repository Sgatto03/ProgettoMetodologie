package it.unicam.cs.mpgc.rpg123297.model.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gestisce tutte le arene del gioco e controlla la progressione del giocatore.
 * Calcola quali arene sono sbloccate in base alla fama accumulata.
 */
public class ArenaManager {

    private final List<Arena> arenas;

    public ArenaManager() {
        this.arenas = new ArrayList<>();
        initializeArenas();
    }

    /** Crea le arene per ogni tier disponibile. */
    private void initializeArenas() {
        for (ArenaTier tier : ArenaTier.values()) {
            arenas.add(new Arena(tier));
        }
    }

    /**
     * Restituisce tutte le arene.
     */
    public List<Arena> getAllArenas() {
        return Collections.unmodifiableList(arenas);
    }

    /**
     * Restituisce le arene accessibili in base alla fama del giocatore.
     */
    public List<Arena> getAccessibleArenas(int playerFame) {
        return arenas.stream()
                .filter(arena -> arena.isAccessible(playerFame))
                .toList();
    }

    /**
     * Restituisce l'arena con il tier più alto accessibile al giocatore.
     */
    public Arena getHighestAccessibleArena(int playerFame) {
        List<Arena> accessible = getAccessibleArenas(playerFame);
        return accessible.isEmpty() ? arenas.get(0)
                : accessible.get(accessible.size() - 1);
    }

    /**
     * Restituisce un'arena specifica per tier.
     */
    public Arena getArenaByTier(ArenaTier tier) {
        return arenas.stream()
                .filter(a -> a.getTier() == tier)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Arena non trovata per il tier: " + tier));
    }

    /**
     * Restituisce la prossima arena bloccata (obiettivo del giocatore).
     * 
     * @return l'arena da sbloccare, o null se sono tutte sbloccate
     */
    public Arena getNextLockedArena(int playerFame) {
        return arenas.stream()
                .filter(a -> !a.isAccessible(playerFame))
                .findFirst()
                .orElse(null);
    }
}
