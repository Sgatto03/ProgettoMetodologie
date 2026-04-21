package it.unicam.cs.mpgc.rpg123297.model.combat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registro cronologico degli eventi di un combattimento.
 * Ogni voce rappresenta un evento (attacco, difesa, effetto, ecc.).
 */
public class CombatLog {

    private final List<String> entries;

    public CombatLog() {
        this.entries = new ArrayList<>();
    }

    /** Aggiunge una voce al log. */
    public void addEntry(String entry) {
        entries.add(entry);
    }

    /** Restituisce tutte le voci del log. */
    public List<String> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    /** Restituisce l'ultima voce del log. */
    public String getLastEntry() {
        return entries.isEmpty() ? "" : entries.get(entries.size() - 1);
    }

    /** Restituisce il numero di voci. */
    public int size() {
        return entries.size();
    }

    /** Svuota il log. */
    public void clear() {
        entries.clear();
    }

    @Override
    public String toString() {
        return String.join("\n", entries);
    }
}
