package it.unicam.cs.mpgc.rpg123297.model.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Gestisce l'inventario degli oggetti posseduti dal giocatore.
 * Supporta aggiunta, rimozione, ricerca e filtraggio degli oggetti.
 */
public class Inventory {

    private final List<Item> items;
    private static final int MAX_SIZE = 50;

    public Inventory() {
        this.items = new ArrayList<>();
    }

    /**
     * Aggiunge un oggetto all'inventario.
     * 
     * @return true se l'oggetto è stato aggiunto, false se l'inventario è pieno
     */
    public boolean addItem(Item item) {
        if (items.size() >= MAX_SIZE) {
            return false;
        }
        return items.add(item);
    }

    /**
     * Rimuove un oggetto dall'inventario per id.
     * 
     * @return true se l'oggetto è stato trovato e rimosso
     */
    public boolean removeItem(String itemId) {
        return items.removeIf(i -> i.getId().equals(itemId));
    }

    /**
     * Rimuove un oggetto specifico dall'inventario.
     */
    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    /**
     * Cerca un oggetto per id.
     */
    public Optional<Item> findById(String itemId) {
        return items.stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst();
    }

    /**
     * Restituisce tutti gli oggetti di un certo tipo.
     */
    @SuppressWarnings("unchecked")
    public <T extends Item> List<T> getItemsByType(Class<T> type) {
        return items.stream()
                .filter(type::isInstance)
                .map(i -> (T) i)
                .collect(Collectors.toList());
    }

    /**
     * Restituisce tutti gli oggetti di una certa rarità.
     */
    public List<Item> getItemsByRarity(ItemRarity rarity) {
        return items.stream()
                .filter(i -> i.getRarity() == rarity)
                .collect(Collectors.toList());
    }

    /** Restituisce una lista non modificabile di tutti gli oggetti. */
    public List<Item> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    /** Restituisce il numero di oggetti nell'inventario. */
    public int size() {
        return items.size();
    }

    /** Verifica se l'inventario è pieno. */
    public boolean isFull() {
        return items.size() >= MAX_SIZE;
    }

    /** Verifica se l'inventario è vuoto. */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /** Svuota l'inventario. */
    public void clear() {
        items.clear();
    }
}
