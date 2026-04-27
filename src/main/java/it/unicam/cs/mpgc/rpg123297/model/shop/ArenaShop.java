package it.unicam.cs.mpgc.rpg123297.model.shop;

import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;
import it.unicam.cs.mpgc.rpg123297.model.item.Item;
import it.unicam.cs.mpgc.rpg123297.model.item.ItemFactory;

import java.util.Collections;
import java.util.List;

/**
 * Implementazione del negozio legata a un livello di arena (tier).
 * Il catalogo varia in base al tier dell'arena: tier più alti offrono oggetti
 * migliori.
 * I prezzi sono influenzati dal Carisma del gladiatore (sconto).
 */
public class ArenaShop implements Shop {

    private static final double SELL_PRICE_RATIO = 0.5; // 50% del valore

    private final String name;
    private final int tier;
    private final List<Item> catalog;

    /**
     * Crea un negozio per il tier specificato.
     * 
     * @param tier il livello dell'arena associata (1-5)
     */
    public ArenaShop(int tier) {
        this.tier = tier;
        this.name = "Mercato dell'Arena - Tier " + tier;
        this.catalog = ItemFactory.getAllItemsForTier(tier);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Item> getCatalog() {
        return Collections.unmodifiableList(catalog);
    }

    @Override
    public int calculateBuyPrice(Item item, Gladiator gladiator) {
        double discount = gladiator.getStats().calculateShopDiscount() / 100.0;
        return (int) Math.ceil(item.getValue() * (1.0 - discount));
    }

    @Override
    public int calculateSellPrice(Item item) {
        return (int) (item.getValue() * SELL_PRICE_RATIO);
    }

    @Override
    public Transaction buyItem(Item item, Gladiator gladiator) {
        int price = calculateBuyPrice(item, gladiator);

        // Verifica che l'oggetto sia nel catalogo
        if (!catalog.contains(item)) {
            return Transaction.failure(Transaction.TransactionType.BUY, item,
                    "Questo oggetto non è disponibile in questo negozio.");
        }

        // Verifica oro sufficiente
        if (!gladiator.spendGold(price)) {
            return Transaction.failure(Transaction.TransactionType.BUY, item,
                    String.format("Oro insufficiente! Servono %d oro, ne hai %d.",
                            price, gladiator.getGold()));
        }

        // Verifica spazio inventario
        if (gladiator.getInventory().isFull()) {
            gladiator.addGold(price); // refund
            return Transaction.failure(Transaction.TransactionType.BUY, item,
                    "Inventario pieno! Vendi qualcosa prima di acquistare.");
        }

        gladiator.getInventory().addItem(item);
        return Transaction.success(Transaction.TransactionType.BUY, item, price);
    }

    @Override
    public Transaction sellItem(Item item, Gladiator gladiator) {
        // Verifica che il giocatore possieda l'oggetto
        if (!gladiator.getInventory().getAllItems().contains(item)) {
            return Transaction.failure(Transaction.TransactionType.SELL, item,
                    "Non possiedi questo oggetto.");
        }

        int price = calculateSellPrice(item);
        gladiator.getInventory().removeItem(item);
        gladiator.addGold(price);

        return Transaction.success(Transaction.TransactionType.SELL, item, price);
    }

    public int getTier() {
        return tier;
    }
}
