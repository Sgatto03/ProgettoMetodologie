package it.unicam.cs.mpgc.rpg123297.view.fx.screen;

import it.unicam.cs.mpgc.rpg123297.controller.GameController;
import it.unicam.cs.mpgc.rpg123297.model.arena.Arena;
import it.unicam.cs.mpgc.rpg123297.model.arena.ArenaChallenge;
import it.unicam.cs.mpgc.rpg123297.view.GameView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Schermata di selezione dell'arena e della sfida da affrontare.
 * Mostra le arene sbloccate e quelle ancora bloccate.
 */
public class ArenaSelectionScreen implements GameView {

    private final VBox root;
    private final GameController controller;
    private final VBox arenasContainer;

    public ArenaSelectionScreen(GameController controller, Runnable onBack) {
        this.controller = controller;

        root = new VBox(15);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #0d0d0d;");

        Label title = new Label("🏟️ Scegli l'Arena");
        title.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 24px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Georgia';");

        Label fameLabel = new Label("Fama attuale: " + controller.getGladiator().getFame());
        fameLabel.setStyle("-fx-text-fill: #ff8844; -fx-font-size: 14px;");

        arenasContainer = new VBox(15);
        arenasContainer.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(arenasContainer);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #0d0d0d; -fx-background-color: #0d0d0d;");

        Button backBtn = new Button("← Torna all'Hub");
        backBtn.setId("btn-back-hub");
        backBtn.setStyle("-fx-background-color: #333333; -fx-text-fill: #cccccc; " +
                "-fx-font-size: 13px; -fx-background-radius: 6; -fx-cursor: hand;");
        backBtn.setOnAction(e -> onBack.run());

        root.getChildren().addAll(title, fameLabel, scroll, backBtn);
        refresh();
    }

    @Override
    public void refresh() {
        arenasContainer.getChildren().clear();
        List<Arena> allArenas = controller.getArenaManager().getAllArenas();
        int playerFame = controller.getGladiator().getFame();

        for (Arena arena : allArenas) {
            boolean accessible = arena.isAccessible(playerFame);
            VBox arenaCard = createArenaCard(arena, accessible);
            arenasContainer.getChildren().add(arenaCard);
        }
    }

    private VBox createArenaCard(Arena arena, boolean accessible) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setMaxWidth(600);
        card.setAlignment(Pos.CENTER_LEFT);

        String borderColor = accessible ? "#ffd700" : "#333333";
        String bgColor = accessible ? "#1a1500" : "#111111";
        card.setStyle(String.format(
                "-fx-background-color: %s; -fx-background-radius: 8; " +
                "-fx-border-color: %s; -fx-border-radius: 8; -fx-border-width: 2;",
                bgColor, borderColor));

        // Intestazione
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        String tierStars = "⭐".repeat(arena.getTier().getTier());
        Label nameLabel = new Label(tierStars + " " + arena.getName());
        String nameColor = accessible ? "#ffd700" : "#555555";
        nameLabel.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-size: 16px; -fx-font-weight: bold;", nameColor));

        Label fameReq = new Label(String.format("Fama: %d", arena.getFameRequired()));
        fameReq.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 12px;",
                accessible ? "#44ff88" : "#aa3333"));

        header.getChildren().addAll(nameLabel, fameReq);

        Label descLabel = new Label(arena.getDescription());
        descLabel.setWrapText(true);
        descLabel.setStyle(String.format("-fx-text-fill: %s; -fx-font-size: 12px;",
                accessible ? "#cccccc" : "#444444"));

        card.getChildren().addAll(header, descLabel);

        // Sfide (solo se accessibile)
        if (accessible) {
            HBox challengeButtons = new HBox(8);
            challengeButtons.setAlignment(Pos.CENTER_LEFT);

            for (ArenaChallenge challenge : arena.getChallenges()) {
                Button btn = new Button(challenge.getName());
                btn.setStyle("-fx-background-color: #332200; -fx-text-fill: #ffd700; " +
                        "-fx-font-size: 12px; -fx-background-radius: 6; -fx-cursor: hand; " +
                        "-fx-border-color: #664400; -fx-border-radius: 6;");

                btn.setOnMouseEntered(e -> btn.setStyle(
                        "-fx-background-color: #553300; -fx-text-fill: #ffdd44; " +
                        "-fx-font-size: 12px; -fx-background-radius: 6; -fx-cursor: hand; " +
                        "-fx-border-color: #ffd700; -fx-border-radius: 6;"));
                btn.setOnMouseExited(e -> btn.setStyle(
                        "-fx-background-color: #332200; -fx-text-fill: #ffd700; " +
                        "-fx-font-size: 12px; -fx-background-radius: 6; -fx-cursor: hand; " +
                        "-fx-border-color: #664400; -fx-border-radius: 6;"));

                btn.setOnAction(e -> {
                    controller.startCombat(challenge);
                    // La navigazione al combattimento è gestita dal FxGameApp
                });

                Tooltip tooltip = new Tooltip(challenge.getDescription());
                btn.setTooltip(tooltip);

                challengeButtons.getChildren().add(btn);
            }
            card.getChildren().add(challengeButtons);
        } else {
            Label lockedLabel = new Label("🔒 Arena bloccata");
            lockedLabel.setStyle("-fx-text-fill: #aa3333; -fx-font-size: 12px; -fx-font-style: italic;");
            card.getChildren().add(lockedLabel);
        }

        return card;
    }

    @Override
    public Parent getRoot() { return root; }

    @Override
    public String getTitle() { return "Arena of Glory — Selezione Arena"; }
}
