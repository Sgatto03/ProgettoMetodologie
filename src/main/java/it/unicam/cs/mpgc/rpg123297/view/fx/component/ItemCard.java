package it.unicam.cs.mpgc.rpg123297.view.fx.component;

import it.unicam.cs.mpgc.rpg123297.model.item.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Componente grafico per visualizzare un oggetto con tutte le sue informazioni.
 */
public class ItemCard extends VBox {

    /**
     * Crea una card per un oggetto.
     * 
     * @param item l'oggetto da visualizzare
     */
    public ItemCard(Item item) {
        setSpacing(4);
        setPadding(new Insets(10));
        setAlignment(Pos.TOP_LEFT);
        setMinWidth(200);
        setMaxWidth(300);

        String rarityColor = item.getRarity().getColorHex();
        setStyle(String.format(
                "-fx-background-color: #1e1e1e; -fx-background-radius: 8; " +
                        "-fx-border-color: %s; -fx-border-radius: 8; -fx-border-width: 2;",
                rarityColor));

        // Nome con colore rarità
        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-size: 14px; -fx-font-weight: bold;",
                rarityColor));

        // Rarità
        Label rarityLabel = new Label(item.getRarity().getDisplayName());
        rarityLabel.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-size: 11px; -fx-font-style: italic;",
                rarityColor));

        // Descrizione
        Label descLabel = new Label(item.getDescription());
        descLabel.setWrapText(true);
        descLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 11px;");

        // Statistiche specifiche
        Label statsLabel = new Label(getItemStats(item));
        statsLabel.setStyle("-fx-text-fill: #00ff88; -fx-font-size: 12px;");

        // Prezzo
        Label priceLabel = new Label(String.format("💰 %d oro", item.getValue()));
        priceLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 12px;");

        getChildren().addAll(nameLabel, rarityLabel, descLabel, statsLabel, priceLabel);
    }

    private String getItemStats(Item item) {
        if (item instanceof Weapon w) {
            return String.format("⚔️ Danno: +%d", w.getDamage());
        } else if (item instanceof Armor a) {
            return String.format("🛡️ Difesa: +%d (%s)", a.getDefense(),
                    a.getSlot().getDisplayName());
        } else if (item instanceof Consumable c) {
            return String.format("🧪 %s: +%d", c.getEffectType().getDisplayName(),
                    c.getEffectValue());
        }
        return "";
    }
}
