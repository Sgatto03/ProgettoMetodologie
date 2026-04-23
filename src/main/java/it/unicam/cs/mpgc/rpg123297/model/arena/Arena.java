package it.unicam.cs.mpgc.rpg123297.model.arena;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Rappresenta un'arena nel gioco.
 * Ogni arena ha un tier, una lista di sfide e requisiti di accesso.
 */
public class Arena {

    private final ArenaTier tier;
    private final List<ArenaChallenge> challenges;

    /**
     * Crea una nuova arena per il tier specificato con sfide predefinite.
     */
    public Arena(ArenaTier tier) {
        this.tier = Objects.requireNonNull(tier);
        this.challenges = new ArrayList<>();
        initializeChallenges();
    }

    /** Inizializza le sfide disponibili per questa arena. */
    private void initializeChallenges() {
        int tierLevel = tier.getTier();
        switch (tier) {
            case STREET -> {
                challenges.add(new ArenaChallenge("Rissa di Strada",
                        "Un combattimento rapido contro un avversario casuale.", tierLevel));
                challenges.add(new ArenaChallenge("Duello della Fossa",
                        "Un duello all'ultimo sangue nel fango.", tierLevel));
                challenges.add(new ArenaChallenge("Sfida del Disperato",
                        "Affronta un combattente senza nulla da perdere.", tierLevel));
            }
            case PROVINCIAL -> {
                challenges.add(new ArenaChallenge("Torneo del Novizio",
                        "Combatti per dimostrare il tuo valore.", tierLevel));
                challenges.add(new ArenaChallenge("Sfida del Mercenario",
                        "Affronta un soldato di ventura esperto.", tierLevel));
                challenges.add(new ArenaChallenge("Prova del Guerriero",
                        "Supera la prova per accedere alle arene superiori.", tierLevel));
            }
            case IMPERIAL -> {
                challenges.add(new ArenaChallenge("Gloria dell'Impero",
                        "Combatti per l'onore dell'Impero.", tierLevel));
                challenges.add(new ArenaChallenge("Vendetta del Veterano",
                        "Un veterano cerca vendetta nell'arena.", tierLevel));
                challenges.add(new ArenaChallenge("Campionato Imperiale",
                        "Il torneo più prestigioso della città.", tierLevel));
            }
            case COLOSSEUM -> {
                challenges.add(new ArenaChallenge("Spettacolo del Colosseo",
                        "Combatti davanti all'Imperatore.", tierLevel));
                challenges.add(new ArenaChallenge("Sfida delle Bestie",
                        "Affronta creature feroci dell'arena.", tierLevel));
                challenges.add(new ArenaChallenge("Duello dei Campioni",
                        "Solo i migliori sopravvivono.", tierLevel));
            }
            case DIVINE -> {
                challenges.add(new ArenaChallenge("Giudizio di Marte",
                        "Il dio della guerra osserva il tuo valore.", tierLevel));
                challenges.add(new ArenaChallenge("Sfida dell'Olimpo",
                        "Combatti contro guerrieri semi-divini.", tierLevel));
                challenges.add(new ArenaChallenge("Ascensione del Gladiatore",
                        "La prova definitiva per diventare una leggenda.", tierLevel));
            }
        }
    }

    public ArenaTier getTier() {
        return tier;
    }

    public String getName() {
        return tier.getDisplayName();
    }

    public String getDescription() {
        return tier.getDescription();
    }

    public int getFameRequired() {
        return tier.getFameRequired();
    }

    /**
     * Restituisce la lista di sfide disponibili.
     */
    public List<ArenaChallenge> getChallenges() {
        return Collections.unmodifiableList(challenges);
    }

    /**
     * Verifica se il giocatore può accedere a questa arena.
     * 
     * @param playerFame la fama corrente del giocatore
     */
    public boolean isAccessible(int playerFame) {
        return playerFame >= tier.getFameRequired();
    }

    @Override
    public String toString() {
        return String.format("%s (Tier %d) - Fama richiesta: %d",
                tier.getDisplayName(), tier.getTier(), tier.getFameRequired());
    }
}
