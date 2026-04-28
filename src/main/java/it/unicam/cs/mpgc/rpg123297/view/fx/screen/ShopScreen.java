package it.unicam.cs.mpgc.rpg123297.view.fx.screen;

import it.unicam.cs.mpgc.rpg123297.controller.GameController;
import it.unicam.cs.mpgc.rpg123297.controller.ShopController;
import it.unicam.cs.mpgc.rpg123297.model.item.*;
import it.unicam.cs.mpgc.rpg123297.model.shop.Transaction;
import it.unicam.cs.mpgc.rpg123297.view.GameView;
import it.unicam.cs.mpgc.rpg123297.view.fx.component.ItemCard;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Schermata del negozio. Mostra il catalogo del negozio (acquisto)
 * e gli oggetti del giocatore (vendita) con prezzi aggiornati.
 */
public class ShopScreen implements GameView {

    private final BorderPane root;
    private final GameController gameController;
    private ShopController shopController;
    private final Label goldLabel;
    private final Label messageLabel;
    private final FlowPane catalogPane;
    private final FlowPane inventoryPane;

    public ShopScreen(GameController controller, int tier, Runnable onBack) {
        this.gameController = controller;
        this.shopController = controller.openShop(tier);

        root = new BorderPane();
        root.setStyle("-fx-background-color: #0d0d0d;");
        root.setPadding(new Insets(15));

        // === TOP ===
        VBox topBox = new VBox(5);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10));

        Label title = new Label("🏪 " + shopController.getShopName());
        title.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Georgia';");

        goldLabel = new Label();
        goldLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 16px; -fx-font-weight: bold;");

        messageLabel = new Label();
        messageLabel.setStyle("-fx-font-size: 13px;");

        Button backBtn = new Button("← Torna all'Hub");
        backBtn.setStyle("-fx-background-color: #333333; -fx-text-fill: #cccccc; " +
                "-fx-font-size: 13px; -fx-background-radius: 6; -fx-cursor: hand;");
        backBtn.setOnAction(e -> onBack.run());

        topBox.getChildren().addAll(title, goldLabel, messageLabel, backBtn);
        root.setTop(topBox);

        // === LEFT: Catalogo (acquisto) ===
        VBox leftBox = new VBox(10);
        leftBox.setPadding(new Insets(10));

        Label shopLabel = new Label("📦 In Vendita");
        shopLabel.setStyle("-fx-text-fill: #44aaff; -fx-font-size: 16px; -fx-font-weight: bold;");

        catalogPane = new FlowPane(10, 10);
        catalogPane.setPadding(new Insets(5));

        ScrollPane leftScroll = new ScrollPane(catalogPane);
        leftScroll.setFitToWidth(true);
        leftScroll.setStyle("-fx-background: #0d0d0d; -fx-background-color: #0d0d0d;");

        leftBox.getChildren().addAll(shopLabel, leftScroll);

        // === RIGHT: Inventario (vendita) ===
        VBox rightBox = new VBox(10);
        rightBox.setPadding(new Insets(10));

        Label invLabel = new Label("🎒 Il Tuo Inventario");
        invLabel.setStyle("-fx-text-fill: #ff8844; -fx-font-size: 16px; -fx-font-weight: bold;");

        inventoryPane = new FlowPane(10, 10);
        inventoryPane.setPadding(new Insets(5));

        ScrollPane rightScroll = new ScrollPane(inventoryPane);
        rightScroll.setFitToWidth(true);
        rightScroll.setStyle("-fx-background: #0d0d0d; -fx-background-color: #0d0d0d;");

        rightBox.getChildren().addAll(invLabel, rightScroll);

        SplitPane splitPane = new SplitPane(leftBox, rightBox);
        splitPane.setDividerPositions(0.55);
        splitPane.setStyle("-fx-background-color: #0d0d0d;");
        root.setCenter(splitPane);

        refresh();
    }

    @Override
    public void refresh() {
        goldLabel.setText("💰 " + shopController.getPlayerGold() + " oro");

        // Catalogo
        catalogPane.getChildren().clear();
        for (Item item : shopController.getCatalog()) {
            VBox itemBox = new VBox(5);
            itemBox.getChildren().add(new ItemCard(item));

            int price = shopController.getBuyPrice(item);
            Button buyBtn = new Button("Compra — " + price + " oro");
            buyBtn.setStyle("-fx-background-color: #004400; -fx-text-fill: #44ff88; " +
                    "-fx-font-size: 12px; -fx-background-radius: 4; -fx-cursor: hand;");
            buyBtn.setOnAction(e -> {
                Transaction tx = shopController.buyItem(item);
                showMessage(tx.message(), tx.success());
                refresh();
            });
            buyBtn.setDisable(shopController.getPlayerGold() < price);

            itemBox.getChildren().add(buyBtn);
            catalogPane.getChildren().add(itemBox);
        }

        // Inventario
        inventoryPane.getChildren().clear();
        for (Item item : shopController.getPlayerItems()) {
            VBox itemBox = new VBox(5);
            itemBox.getChildren().add(new ItemCard(item));

            int sellPrice = shopController.getSellPrice(item);
            Button sellBtn = new Button("Vendi — " + sellPrice + " oro");
            sellBtn.setStyle("-fx-background-color: #440000; -fx-text-fill: #ff8844; " +
                    "-fx-font-size: 12px; -fx-background-radius: 4; -fx-cursor: hand;");
            sellBtn.setOnAction(e -> {
                Transaction tx = shopController.sellItem(item);
                showMessage(tx.message(), tx.success());
                refresh();
            });

            // Aggiungi pulsante equip per armi e armature
            if (item instanceof Weapon weapon) {
                Button equipBtn = createEquipButton(item, weapon.getSlot());
                itemBox.getChildren().addAll(sellBtn, equipBtn);
            } else if (item instanceof Armor armor) {
                Button equipBtn = createEquipButton(item, armor.getSlot());
                itemBox.getChildren().addAll(sellBtn, equipBtn);
            } else {
                itemBox.getChildren().add(sellBtn);
            }

            inventoryPane.getChildren().add(itemBox);
        }
    }

    private Button createEquipButton(Item item, EquipmentSlot slot) {
        Button equipBtn = new Button("Equipaggia → " + slot.getDisplayName());
        equipBtn.setStyle("-fx-background-color: #333366; -fx-text-fill: #aabbff; " +
                "-fx-font-size: 11px; -fx-background-radius: 4; -fx-cursor: hand;");
        equipBtn.setOnAction(e -> {
            gameController.equipItem(item, slot);
            showMessage("Equipaggiato: " + item.getName(), true);
            refresh();
        });
        return equipBtn;
    }

    private void showMessage(String msg, boolean success) {
        messageLabel.setText(msg);
        messageLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 13px;",
                success ? "#44ff88" : "#ff4444"));
    }

    @Override
    public Parent getRoot() { return root; }

    @Override
    public String getTitle() { return "Arena of Glory — Negozio"; }
}
