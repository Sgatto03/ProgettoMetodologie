package it.unicam.cs.mpgc.rpg123297.model.shop;

import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;
import it.unicam.cs.mpgc.rpg123297.model.item.Item;

import java.util.List;

/**
 * Interfaccia per il sistema di negozio.
 * Definisce le operazioni di acquisto e vendita di oggetti.
 * L'interfaccia permette future implementazioni diverse (es. negozio online,
 * mercato nero).
 */
public interface Shop {

    /** Restituisce il nome del negozio. */
    String getName();

    /** Restituisce il catalogo degli oggetti in vendita. */
    List<Item> getCatalog();

    /**
     * Calcola il prezzo di acquisto di un oggetto, applicando eventuali sconti.
     * 
     * @param item      l'oggetto da acquistare
     * @param gladiator il gladiatore acquirente (il carisma influenza il prezzo)
     * @return il prezzo finale
     */
    int calculateBuyPrice(Item item, Gladiator gladiator);

    /**
     * Calcola il prezzo di vendita di un oggetto.
     * 
     * @param item l'oggetto da vendere
     * @return il prezzo di vendita (di solito 50% del valore)
     */
    int calculateSellPrice(Item item);

    /**
     * Esegue l'acquisto di un oggetto.
     * 
     * @return un {@link Transaction} che descrive l'esito
     */
    Transaction buyItem(Item item, Gladiator gladiator);

    /**
     * Esegue la vendita di un oggetto.
     * 
     * @return un {@link Transaction} che descrive l'esito
     */
    Transaction sellItem(Item item, Gladiator gladiator);
}
