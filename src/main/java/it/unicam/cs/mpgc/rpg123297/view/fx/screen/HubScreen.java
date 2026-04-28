package it.unicam.cs.mpgc.rpg123297.view.fx.screen;

import it.unicam.cs.mpgc.rpg123297.controller.GameController;
import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;
import it.unicam.cs.mpgc.rpg123297.model.item.*;
import it.unicam.cs.mpgc.rpg123297.model.progression.FameRank;
import it.unicam.cs.mpgc.rpg123297.view.GameView;
import it.unicam.cs.mpgc.rpg123297.view.fx.component.StatsBar;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Map;

/**
 * Hub centrale del gioco. Mostra lo stato del gladiatore e le azioni disponibili:
 * arene, negozio, equipaggiamento, salvataggio.
 */
public class HubScreen implements GameView {

    private final BorderPane root;
    private final GameController controller;
    private final StatsBar hpBar;
    private final StatsBar xpBar;
    private final StatsBar fameBar;
    private final Label statsLabel;
    private final Label goldLabel;
    private final Label levelLabel;
    private final Label rankLabel;
    private final VBox equipmentBox;

    public HubScreen(GameController controller,
                     Runnable onArena, Runnable onShop,
                     Runnable onSave, Runnable onMainMenu) {
        this.controller = controller;
        root = new BorderPane();
        root.setStyle("-fx-background-color: #0d0d0d;");
        root.setPadding(new Insets(15));

        Gladiator g = controller.getGladiator();

        // === TOP: Titolo e info gladiatore ===
        VBox topBox = new VBox(6);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10));

        Label title = new Label("🏛️ " + g.getName() + " — " +
                g.getGladiatorClass().getDisplayName());
        title.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 22px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Georgia';");

        levelLabel = new Label();
        levelLabel.setStyle("-fx-text-fill: #aaaaaa; -fx-font-size: 14px;");

        rankLabel = new Label();
        rankLabel.setStyle("-fx-text-fill: #ff8844; -fx-font-size: 14px; -fx-font-weight: bold;");

        topBox.getChildren().addAll(title, levelLabel, rankLabel);
        root.setTop(topBox);

        // === LEFT: Statistiche e barre ===
        VBox leftBox = new VBox(12);
        leftBox.setPadding(new Insets(15));
        leftBox.setMinWidth(280);
        leftBox.setStyle("-fx-background-color: #111111; -fx-background-radius: 8;");

        hpBar = new StatsBar("HP", Color.web("#ff4444"), 250, 22);
        xpBar = new StatsBar("XP", Color.web("#44aaff"), 250, 18);
        fameBar = new StatsBar("Fama", Color.web("#ffd700"), 250, 18);

        statsLabel = new Label();
        statsLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 12px;");

        goldLabel = new Label();
        goldLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 16px; -fx-font-weight: bold;");

        Label statsTitle = new Label("📊 Statistiche");
        statsTitle.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 14px; -fx-font-weight: bold;");

        leftBox.getChildren().addAll(statsTitle, hpBar, xpBar, fameBar,
                new Separator(), goldLabel, new Separator(),
                statsLabel);
        root.setLeft(leftBox);

        // === CENTER: Equipaggiamento ===
        VBox centerBox = new VBox(10);
        centerBox.setPadding(new Insets(15));
        centerBox.setAlignment(Pos.TOP_CENTER);

        Label equipTitle = new Label("🛡️ Equipaggiamento");
        equipTitle.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 16px; -fx-font-weight: bold;");

        equipmentBox = new VBox(5);
        centerBox.getChildren().addAll(equipTitle, equipmentBox);

        ScrollPane centerScroll = new ScrollPane(centerBox);
        centerScroll.setFitToWidth(true);
        centerScroll.setStyle("-fx-background: #0d0d0d; -fx-background-color: #0d0d0d;");
        root.setCenter(centerScroll);

        // === RIGHT: Azioni ===
        VBox rightBox = new VBox(15);
        rightBox.setPadding(new Insets(15));
        rightBox.setAlignment(Pos.TOP_CENTER);
        rightBox.setMinWidth(200);
        rightBox.setStyle("-fx-background-color: #111111; -fx-background-radius: 8;");

        Label actionsTitle = new Label("⚡ Azioni");
        actionsTitle.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 14px; -fx-font-weight: bold;");

        Button arenaBtn = createActionButton("⚔️ Arene", "#664400", onArena);
        arenaBtn.setId("btn-arenas");
        Button shopBtn = createActionButton("🏪 Negozio", "#004466", onShop);
        shopBtn.setId("btn-shop");
        Button saveBtn = createActionButton("💾 Salva", "#446600", onSave);
        saveBtn.setId("btn-save");
        Button menuBtn = createActionButton("🏠 Menu", "#442222", onMainMenu);
        menuBtn.setId("btn-main-menu");

        rightBox.getChildren().addAll(actionsTitle, arenaBtn, shopBtn, saveBtn, menuBtn);
        root.setRight(rightBox);

        refresh();
    }

    private Button createActionButton(String text, String bgColor, Runnable action) {
        Button btn = new Button(text);
        btn.setPrefWidth(170);
        btn.setPrefHeight(42);
        String style = String.format(
                "-fx-background-color: %s; -fx-text-fill: white; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-cursor: hand;", bgColor);
        btn.setStyle(style);
        btn.setOnAction(e -> action.run());
        return btn;
    }

    @Override
    public void refresh() {
        Gladiator g = controller.getGladiator();
        if (g == null) return;

        levelLabel.setText(String.format("Livello %d • XP: %d/%d",
                g.getLevel(), g.getExperience(), g.getExperienceForNextLevel()));

        FameRank rank = controller.getCurrentFameRank();
        rankLabel.setText("🏆 Rango: " + rank.getDisplayName());

        hpBar.update(g.getCurrentHp(), g.getMaxHp());
        xpBar.update(g.getExperience(), g.getExperienceForNextLevel());

        FameRank nextRank = rank.getNextRank();
        if (nextRank != null) {
            fameBar.update(controller.getProgressionManager().getProgressToNextRank(g),
                    String.format("Fama: %d / %d (%s)",
                            g.getFame(), nextRank.getFameRequired(), nextRank.getDisplayName()));
        } else {
            fameBar.update(1.0, "Fama: " + g.getFame() + " (Rango Massimo!)");
        }

        goldLabel.setText("💰 " + g.getGold() + " oro");

        statsLabel.setText(String.format(
                "Forza: %d\nAgilità: %d\nResistenza: %d\nDestrezza: %d\nCarisma: %d\n\n" +
                "Danno: %d\nDifesa: %d\nCritico: %.0f%%\nSchivata: %.0f%%",
                g.getStats().getStrength(), g.getStats().getAgility(),
                g.getStats().getEndurance(), g.getStats().getDexterity(),
                g.getStats().getCharisma(),
                g.calculateAttackDamage(), g.calculateDefense(),
                g.getStats().calculateCritChance(), g.getStats().calculateDodgeChance()));

        updateEquipmentDisplay(g);
    }

    private void updateEquipmentDisplay(Gladiator g) {
        equipmentBox.getChildren().clear();
        Map<EquipmentSlot, Item> equipped = g.getEquipment().getAllEquipped();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            HBox row = new HBox(10);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(5, 10, 5, 10));
            row.setStyle("-fx-background-color: #1a1a1a; -fx-background-radius: 4;");

            Label slotLabel = new Label(slot.getDisplayName() + ":");
            slotLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 12px; -fx-min-width: 120;");

            Item item = equipped.get(slot);
            if (item != null) {
                Label itemLabel = new Label(item.getName());
                itemLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 12px;",
                        item.getRarity().getColorHex()));

                String bonus = "";
                if (item instanceof Weapon w) bonus = " (+" + w.getDamage() + " dmg)";
                else if (item instanceof Armor a) bonus = " (+" + a.getDefense() + " def)";
                Label bonusLabel = new Label(bonus);
                bonusLabel.setStyle("-fx-text-fill: #44ff88; -fx-font-size: 11px;");

                row.getChildren().addAll(slotLabel, itemLabel, bonusLabel);
            } else {
                Label emptyLabel = new Label("— vuoto —");
                emptyLabel.setStyle("-fx-text-fill: #444444; -fx-font-size: 12px; -fx-font-style: italic;");
                row.getChildren().addAll(slotLabel, emptyLabel);
            }

            equipmentBox.getChildren().add(row);
        }
    }

    @Override
    public Parent getRoot() { return root; }

    @Override
    public String getTitle() { return "Arena of Glory — Hub"; }
}
