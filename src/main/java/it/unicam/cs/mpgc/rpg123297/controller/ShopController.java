package it.unicam.cs.mpgc.rpg123297.controller;

import it.unicam.cs.mpgc.rpg123297.event.EventType;
import it.unicam.cs.mpgc.rpg123297.event.GameEventBus;
import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;
import it.unicam.cs.mpgc.rpg123297.model.item.Item;
import it.unicam.cs.mpgc.rpg123297.model.shop.Shop;
import it.unicam.cs.mpgc.rpg123297.model.shop.Transaction;

import java.util.List;

/**
 * Controller per la gestione delle interazioni con il negozio.
 * Si pone tra la view e il model {@link Shop}.
 */
public class ShopController {

    private final Shop shop;
    private final Gladiator gladiator;
    private final GameEventBus eventBus;

    public ShopController(Shop shop, Gladiator gladiator) {
        this.shop = shop;
        this.gladiator = gladiator;
        this.eventBus = GameEventBus.getInstance();
    }

    /** Restituisce il catalogo del negozio. */
    public List<Item> getCatalog() {
        return shop.getCatalog();
    }

    /** Restituisce il nome del negozio. */
    public String getShopName() {
        return shop.getName();
    }

    /** Calcola il prezzo d'acquisto. */
    public int getBuyPrice(Item item) {
        return shop.calculateBuyPrice(item, gladiator);
    }

    /** Calcola il prezzo di vendita. */
    public int getSellPrice(Item item) {
        return shop.calculateSellPrice(item);
    }

    /** Acquista un oggetto. */
    public Transaction buyItem(Item item) {
        Transaction tx = shop.buyItem(item, gladiator);
        if (tx.success()) {
            eventBus.publish(EventType.ITEM_PURCHASED, tx);
        }
        return tx;
    }

    /** Vende un oggetto. */
    public Transaction sellItem(Item item) {
        Transaction tx = shop.sellItem(item, gladiator);
        if (tx.success()) {
            eventBus.publish(EventType.ITEM_SOLD, tx);
        }
        return tx;
    }

    /** Restituisce gli oggetti vendibili dal giocatore. */
    public List<Item> getPlayerItems() {
        return gladiator.getInventory().getAllItems();
    }

    /** Oro corrente del giocatore. */
    public int getPlayerGold() {
        return gladiator.getGold();
    }
}
