package it.unicam.cs.mpgc.rpg123297.view.fx.screen;

import it.unicam.cs.mpgc.rpg123297.view.GameView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Schermata di fine partita (vittoria campagna o sconfitta).
 */
public class GameOverScreen implements GameView {

    private final VBox root;

    public GameOverScreen(boolean victory, Runnable onRestart, Runnable onMainMenu) {
        root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0d0d0d, " +
                (victory ? "#1a1500" : "#1a0000") + ", #0d0d0d);");

        String titleText = victory ? "🏆 GLORIA ETERNA! 🏆" : "☠️ LA TUA AVVENTURA FINISCE ☠️";
        String titleColor = victory ? "#ffd700" : "#ff4444";

        Label title = new Label(titleText);
        title.setStyle(String.format(
                "-fx-text-fill: %s; -fx-font-size: 32px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Georgia';", titleColor));
        title.setEffect(new DropShadow(15, Color.web(titleColor)));

        String message = victory
                ? "Hai raggiunto la gloria suprema nell'Arena degli Dei!\nIl tuo nome sarà ricordato per l'eternità."
                : "Il gladiatore è caduto nell'arena.\nMa la sua memoria vivrà nei canti dei bardi.";

        Label msgLabel = new Label(message);
        msgLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 16px; -fx-text-alignment: center;");
        msgLabel.setWrapText(true);

        Button restartBtn = new Button("🗡️ Nuova Partita");
        restartBtn.setPrefWidth(250);
        restartBtn.setPrefHeight(45);
        restartBtn.setStyle(
                "-fx-background-color: #664400; -fx-text-fill: #ffd700; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-cursor: hand;");
        restartBtn.setOnAction(e -> onRestart.run());

        Button menuBtn = new Button("🏠 Menu Principale");
        menuBtn.setPrefWidth(250);
        menuBtn.setPrefHeight(45);
        menuBtn.setStyle(
                "-fx-background-color: #333333; -fx-text-fill: #cccccc; " +
                "-fx-font-size: 16px; -fx-background-radius: 8; -fx-cursor: hand;");
        menuBtn.setOnAction(e -> onMainMenu.run());

        root.getChildren().addAll(title, msgLabel, restartBtn, menuBtn);
    }

    @Override
    public Parent getRoot() { return root; }

    @Override
    public void refresh() { }

    @Override
    public String getTitle() { return "Arena of Glory — Fine"; }
}
