package it.unicam.cs.mpgc.rpg123297.model.shop;

import it.unicam.cs.mpgc.rpg123297.model.item.Item;

/**
 * Rappresenta l'esito di una transazione commerciale (acquisto o vendita).
 * È un record immutabile che descrive cosa è successo.
 */
public record Transaction(
        TransactionType type,
        Item item,
        int price,
        boolean success,
        String message) {
    /**
     * Tipo di transazione.
     */
    public enum TransactionType {
        BUY("Acquisto"),
        SELL("Vendita");

        private final String displayName;

        TransactionType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /** Crea una transazione riuscita. */
    public static Transaction success(TransactionType type, Item item, int price) {
        String msg = type == TransactionType.BUY
                ? String.format("Acquistato %s per %d oro.", item.getName(), price)
                : String.format("Venduto %s per %d oro.", item.getName(), price);
        return new Transaction(type, item, price, true, msg);
    }

    /** Crea una transazione fallita. */
    public static Transaction failure(TransactionType type, Item item, String reason) {
        return new Transaction(type, item, 0, false, reason);
    }
}
