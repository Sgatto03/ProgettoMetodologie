package it.unicam.cs.mpgc.rpg123297.view.fx.component;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Pannello scrollabile per visualizzare il log del combattimento.
 * Mostra gli eventi in ordine cronologico con auto-scroll verso il basso.
 */
public class CombatLogPanel extends VBox {

    private final VBox logContainer;
    private final ScrollPane scrollPane;

    public CombatLogPanel() {
        logContainer = new VBox(2);
        logContainer.setPadding(new Insets(8));
        logContainer.setStyle("-fx-background-color: #0a0a0a;");

        scrollPane = new ScrollPane(logContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle(
                "-fx-background: #0a0a0a; -fx-background-color: #0a0a0a; " +
                        "-fx-border-color: #333333; -fx-border-radius: 4;");

        Label title = new Label("📜 Log di Combattimento");
        title.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 13px; -fx-font-weight: bold;");

        setSpacing(4);
        getChildren().addAll(title, scrollPane);
    }

    /**
     * Aggiorna il log con tutte le voci correnti.
     */
    public void updateLog(List<String> entries) {
        logContainer.getChildren().clear();
        for (String entry : entries) {
            Label entryLabel = new Label(entry);
            entryLabel.setWrapText(true);
            entryLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 11px;");

            // Colora in base al tipo di evento
            if (entry.contains("CRITICO") || entry.contains("💥")) {
                entryLabel.setStyle("-fx-text-fill: #ff4444; -fx-font-size: 11px; -fx-font-weight: bold;");
            } else if (entry.contains("🏆") || entry.contains("⬆️")) {
                entryLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 11px; -fx-font-weight: bold;");
            } else if (entry.contains("☠️")) {
                entryLabel.setStyle("-fx-text-fill: #ff0000; -fx-font-size: 12px; -fx-font-weight: bold;");
            } else if (entry.contains("🛡️") || entry.contains("🏰")) {
                entryLabel.setStyle("-fx-text-fill: #4488ff; -fx-font-size: 11px;");
            } else if (entry.contains("🧪")) {
                entryLabel.setStyle("-fx-text-fill: #44ff88; -fx-font-size: 11px;");
            } else if (entry.contains("===")) {
                entryLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 12px; -fx-font-weight: bold;");
            }

            logContainer.getChildren().add(entryLabel);
        }

        // Auto-scroll al fondo
        scrollPane.layout();
        scrollPane.setVvalue(1.0);
    }
}
