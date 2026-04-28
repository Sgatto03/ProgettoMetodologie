package it.unicam.cs.mpgc.rpg123297.view.fx.screen;

import it.unicam.cs.mpgc.rpg123297.controller.CombatController;
import it.unicam.cs.mpgc.rpg123297.controller.GameController;
import it.unicam.cs.mpgc.rpg123297.model.character.Enemy;
import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;
import it.unicam.cs.mpgc.rpg123297.model.combat.CombatResult;
import it.unicam.cs.mpgc.rpg123297.model.item.Consumable;
import it.unicam.cs.mpgc.rpg123297.view.GameView;
import it.unicam.cs.mpgc.rpg123297.view.fx.component.CombatLogPanel;
import it.unicam.cs.mpgc.rpg123297.view.fx.component.StatsBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Schermata di combattimento a turni.
 * Visualizza i due combattenti, le barre HP, i pulsanti azione e il log.
 */
public class CombatScreen implements GameView {

    private final BorderPane root;
    private final GameController gameController;
    private final Runnable onCombatEnd;

    private CombatController combatController;
    private StatsBar playerHpBar;
    private StatsBar enemyHpBar;
    private CombatLogPanel logPanel;
    private HBox actionButtons;
    private VBox resultBox;

    public CombatScreen(GameController controller, Runnable onCombatEnd) {
        this.gameController = controller;
        this.onCombatEnd = onCombatEnd;

        root = new BorderPane();
        root.setStyle("-fx-background-color: #0a0000;");
        root.setPadding(new Insets(15));

        buildUI();
    }

    private void buildUI() {
        combatController = gameController.getCombatController();
        Gladiator gladiator = combatController.getGladiator();
        Enemy enemy = combatController.getEnemy();

        // === TOP: Titolo combattimento ===
        Label title = new Label("⚔️ COMBATTIMENTO ⚔️");
        title.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 20px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Georgia';");
        BorderPane.setAlignment(title, Pos.CENTER);
        root.setTop(title);

        // === CENTER: Combattenti + Log ===
        VBox centerBox = new VBox(15);
        centerBox.setAlignment(Pos.CENTER);

        // Combattenti
        HBox combatantsRow = new HBox(40);
        combatantsRow.setAlignment(Pos.CENTER);

        // Giocatore
        VBox playerBox = createCombatantBox(gladiator.getName(), gladiator,
                Color.web("#44aaff"), true);
        playerHpBar = (StatsBar) playerBox.getChildren().stream()
                .filter(n -> n instanceof StatsBar).findFirst().orElse(null);

        // VS
        Label vsLabel = new Label("VS");
        vsLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 28px; -fx-font-weight: bold;");

        // Nemico
        VBox enemyBox = createCombatantBox(enemy.getName(), enemy,
                Color.web("#ff4444"), false);
        enemyHpBar = (StatsBar) enemyBox.getChildren().stream()
                .filter(n -> n instanceof StatsBar).findFirst().orElse(null);
        Label enemyDescLabel = new Label(enemy.getDescription());
        enemyDescLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px; -fx-font-style: italic;");
        enemyBox.getChildren().add(1, enemyDescLabel);

        combatantsRow.getChildren().addAll(playerBox, vsLabel, enemyBox);

        // Log
        logPanel = new CombatLogPanel();

        // Result (inizialmente nascosto)
        resultBox = new VBox(10);
        resultBox.setAlignment(Pos.CENTER);
        resultBox.setVisible(false);

        centerBox.getChildren().addAll(combatantsRow, logPanel, resultBox);
        root.setCenter(centerBox);

        // === BOTTOM: Pulsanti azione ===
        actionButtons = new HBox(12);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.setPadding(new Insets(15));

        Button attackBtn = createCombatButton("⚔️ Attacca", "#663300",
                () -> { combatController.playerAttack(); refresh(); });
        attackBtn.setId("btn-attack");

        Button defendBtn = createCombatButton("🛡️ Difendi", "#003366",
                () -> { combatController.playerDefend(); refresh(); });
        defendBtn.setId("btn-defend");

        Button specialBtn = createCombatButton(
                "✨ " + gladiator.getGladiatorClass().getSpecialAbilityName(), "#660066",
                () -> { combatController.playerSpecial(); refresh(); });
        specialBtn.setId("btn-special");

        Button itemBtn = createCombatButton("🧪 Usa Oggetto", "#006633", () -> {
            showItemSelection();
        });
        itemBtn.setId("btn-use-item");

        actionButtons.getChildren().addAll(attackBtn, defendBtn, specialBtn, itemBtn);
        root.setBottom(actionButtons);

        refresh();
    }

    private VBox createCombatantBox(String name,
                                     it.unicam.cs.mpgc.rpg123297.model.character.GameCharacter character,
                                     Color hpColor, boolean isPlayer) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(15));
        box.setMinWidth(250);
        box.setStyle("-fx-background-color: #111111; -fx-background-radius: 8; " +
                "-fx-border-color: #333333; -fx-border-radius: 8;");

        Label nameLabel = new Label(name);
        nameLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 16px; -fx-font-weight: bold;",
                isPlayer ? "#44aaff" : "#ff4444"));

        StatsBar hpBar = new StatsBar("HP", hpColor, 220, 20);
        hpBar.update(character.getCurrentHp(), character.getMaxHp());

        Label statsLabel = new Label(String.format("ATK: %d  DEF: %d",
                character.calculateAttackDamage(), character.calculateDefense()));
        statsLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 11px;");

        box.getChildren().addAll(nameLabel, hpBar, statsLabel);
        return box;
    }

    private Button createCombatButton(String text, String bgColor, Runnable action) {
        Button btn = new Button(text);
        btn.setPrefWidth(160);
        btn.setPrefHeight(40);
        btn.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-size: 13px; -fx-font-weight: bold; " +
                "-fx-background-radius: 6; -fx-cursor: hand;", bgColor));
        btn.setOnAction(e -> action.run());
        return btn;
    }

    private void showItemSelection() {
        Gladiator g = combatController.getGladiator();
        List<Consumable> consumables = g.getInventory().getItemsByType(Consumable.class);

        if (consumables.isEmpty()) {
            logPanel.updateLog(combatController.getLog().getEntries());
            return;
        }

        Dialog<Consumable> dialog = new Dialog<>();
        dialog.setTitle("Usa Oggetto");
        dialog.setHeaderText("Scegli un consumabile da usare:");

        // Lista oggetti selezionabili
        ListView<Consumable> listView = new ListView<>();
        listView.getItems().addAll(consumables);
        listView.setPrefHeight(Math.min(consumables.size() * 40 + 10, 200));
        listView.setPrefWidth(400);
        listView.getSelectionModel().selectFirst();
        listView.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: white;");

        dialog.getDialogPane().setContent(listView);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Doppio click per confermare
        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2 && listView.getSelectionModel().getSelectedItem() != null) {
                dialog.setResult(listView.getSelectionModel().getSelectedItem());
                dialog.close();
            }
        });

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                return listView.getSelectionModel().getSelectedItem();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(item -> {
            combatController.playerUseItem(item);
            refresh();
        });
    }

    @Override
    public void refresh() {
        if (combatController == null) return;

        Gladiator g = combatController.getGladiator();
        Enemy e = combatController.getEnemy();

        if (playerHpBar != null) playerHpBar.update(g.getCurrentHp(), g.getMaxHp());
        if (enemyHpBar != null) enemyHpBar.update(e.getCurrentHp(), e.getMaxHp());

        logPanel.updateLog(combatController.getLog().getEntries());

        // Aggiorna disponibilità dei pulsanti
        if (actionButtons != null) {
            boolean canAct = !combatController.isCombatOver();
            actionButtons.getChildren().forEach(node -> {
                if (node instanceof Button btn) {
                    btn.setDisable(!canAct);
                }
            });

            // Speciale disponibile solo se pronto
            actionButtons.getChildren().stream()
                    .filter(n -> n instanceof Button && "btn-special".equals(((Button) n).getId()))
                    .findFirst()
                    .ifPresent(n -> ((Button) n).setDisable(!canAct || !combatController.isSpecialAvailable()));

            // Consumabili disponibili solo se ne ha
            actionButtons.getChildren().stream()
                    .filter(n -> n instanceof Button && "btn-use-item".equals(((Button) n).getId()))
                    .findFirst()
                    .ifPresent(n -> ((Button) n).setDisable(!canAct || !combatController.hasConsumables()));
        }

        // Mostra risultato a fine combattimento
        if (combatController.isCombatOver()) {
            showResult();
        }
    }

    private void showResult() {
        CombatResult result = combatController.getResult();
        resultBox.getChildren().clear();
        resultBox.setVisible(true);

        String outcomeText = result.isVictory() ? "🏆 VITTORIA! 🏆" : "☠️ SCONFITTA ☠️";
        String outcomeColor = result.isVictory() ? "#ffd700" : "#ff0000";

        Label outcomeLabel = new Label(outcomeText);
        outcomeLabel.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-size: 24px; -fx-font-weight: bold;", outcomeColor));

        resultBox.getChildren().add(outcomeLabel);

        if (result.isVictory()) {
            Label rewardsLabel = new Label(String.format(
                    "💰 %d oro   ⭐ %d XP   🏆 %d fama",
                    result.getGoldEarned(), result.getXpEarned(), result.getFameEarned()));
            rewardsLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 14px;");
            resultBox.getChildren().add(rewardsLabel);

            if (result.isLeveledUp()) {
                Label lvlUpLabel = new Label("⬆️ LEVEL UP!");
                lvlUpLabel.setStyle("-fx-text-fill: #44aaff; -fx-font-size: 18px; -fx-font-weight: bold;");
                resultBox.getChildren().add(lvlUpLabel);
            }
        }

        Button continueBtn = new Button("Continua →");
        continueBtn.setId("btn-continue");
        continueBtn.setStyle(
                "-fx-background-color: #664400; -fx-text-fill: #ffd700; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-radius: 6; -fx-cursor: hand;");
        continueBtn.setOnAction(e -> onCombatEnd.run());
        resultBox.getChildren().add(continueBtn);
    }

    @Override
    public Parent getRoot() { return root; }

    @Override
    public String getTitle() { return "Arena of Glory — Combattimento"; }
}
