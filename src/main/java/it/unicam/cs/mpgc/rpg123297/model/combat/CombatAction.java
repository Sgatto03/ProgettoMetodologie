package it.unicam.cs.mpgc.rpg123297.model.combat;

import it.unicam.cs.mpgc.rpg123297.model.character.GameCharacter;

/**
 * Interfaccia Strategy per le azioni di combattimento.
 * Ogni azione concreta implementa questa interfaccia con la propria logica.
 * Nuove azioni possono essere aggiunte senza modificare il codice esistente
 * (OCP).
 */
public interface CombatAction {

    /**
     * Esegue l'azione di combattimento.
     *
     * @param attacker il personaggio che esegue l'azione
     * @param defender il personaggio che subisce l'azione
     * @param log      il log di combattimento dove registrare gli eventi
     * @return l'ammontare di danno effettivamente inflitto (0 se azione non
     *         offensiva)
     */
    int execute(GameCharacter attacker, GameCharacter defender, CombatLog log);

    /** Restituisce il nome dell'azione. */
    String getName();

    /** Restituisce la descrizione dell'azione. */
    String getDescription();

    /** Verifica se l'azione è disponibile per il personaggio. */
    boolean isAvailable(GameCharacter character);
}
