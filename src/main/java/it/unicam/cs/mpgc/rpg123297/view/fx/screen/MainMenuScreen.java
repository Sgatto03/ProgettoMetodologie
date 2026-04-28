package it.unicam.cs.mpgc.rpg123297.view.fx.screen;

import it.unicam.cs.mpgc.rpg123297.controller.GameController;
import it.unicam.cs.mpgc.rpg123297.view.GameView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

/**
 * Schermata del menu principale del gioco.
 * Mostra il titolo, le opzioni per nuova partita, caricamento e uscita.
 */
public class MainMenuScreen implements GameView {

    private final VBox root;

    public MainMenuScreen(GameController controller,
                          Runnable onNewGame, Runnable onLoadGame, Runnable onExit) {
        this.root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0d0d0d, #1a0a00, #0d0d0d);");

        // Titolo
        Label title = new Label("⚔️ ARENA OF GLORY ⚔️");
        title.setStyle(
                "-fx-text-fill: #ffd700; -fx-font-size: 42px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Georgia';");
        title.setEffect(new DropShadow(20, Color.web("#ff6600")));

        Label subtitle = new Label("La Gloria del Gladiatore");
        subtitle.setStyle(
                "-fx-text-fill: #cc9933; -fx-font-size: 18px; -fx-font-style: italic; " +
                "-fx-font-family: 'Georgia';");

        // Spaziatore
        VBox spacer = new VBox();
        spacer.setMinHeight(40);

        // Pulsanti
        Button newGameBtn = createMenuButton("🗡️  Nuova Partita", onNewGame);
        newGameBtn.setId("btn-new-game");

        Button loadGameBtn = createMenuButton("📂  Carica Partita", onLoadGame);
        loadGameBtn.setId("btn-load-game");
        // Disabilita se non ci sono salvataggi
        List<String> saves = controller.listSaves();
        loadGameBtn.setDisable(saves.isEmpty());

        Button exitBtn = createMenuButton("🚪  Esci", onExit);
        exitBtn.setId("btn-exit");
        exitBtn.setStyle(exitBtn.getStyle() + "-fx-border-color: #aa3333;");

        // Crediti
        Label credits = new Label("Progetto MDP & MGC — A.A. 2025/26");
        credits.setStyle("-fx-text-fill: #555555; -fx-font-size: 11px;");

        root.getChildren().addAll(title, subtitle, spacer,
                newGameBtn, loadGameBtn, exitBtn,
                new VBox(30), credits);
    }

    private Button createMenuButton(String text, Runnable action) {
        Button btn = new Button(text);
        btn.setPrefWidth(280);
        btn.setPrefHeight(50);
        btn.setStyle(
                "-fx-background-color: #1a1a1a; -fx-text-fill: #ffd700; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-border-radius: 8; " +
                "-fx-border-color: #664400; -fx-border-width: 2; -fx-cursor: hand;");

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #332200; -fx-text-fill: #ffdd44; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-border-radius: 8; " +
                "-fx-border-color: #ffd700; -fx-border-width: 2; -fx-cursor: hand;"));

        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #1a1a1a; -fx-text-fill: #ffd700; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-border-radius: 8; " +
                "-fx-border-color: #664400; -fx-border-width: 2; -fx-cursor: hand;"));

        btn.setOnAction(e -> action.run());
        return btn;
    }

    @Override
    public Parent getRoot() { return root; }

    @Override
    public void refresh() {
        // Aggiorna lo stato del pulsante carica
    }

    @Override
    public String getTitle() { return "Arena of Glory — Menu Principale"; }
}
