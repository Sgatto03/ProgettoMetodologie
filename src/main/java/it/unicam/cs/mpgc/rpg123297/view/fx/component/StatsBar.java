package it.unicam.cs.mpgc.rpg123297.view.fx.component;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Componente grafico personalizzato per visualizzare una barra
 * (HP, XP, fama) con colore, valore corrente e massimo.
 */
public class StatsBar extends StackPane {

    private final Rectangle background;
    private final Rectangle fill;
    private final Label label;
    private final String statName;

    /**
     * Crea una nuova barra di statistiche.
     * @param statName nome della statistica visualizzata
     * @param color    colore della barra di riempimento
     * @param width    larghezza della barra
     * @param height   altezza della barra
     */
    public StatsBar(String statName, Color color, double width, double height) {
        this.statName = statName;

        background = new Rectangle(width, height);
        background.setFill(Color.web("#2a2a2a"));
        background.setArcWidth(8);
        background.setArcHeight(8);
        background.setStroke(Color.web("#555555"));
        background.setStrokeWidth(1);

        fill = new Rectangle(0, height);
        fill.setFill(color);
        fill.setArcWidth(8);
        fill.setArcHeight(8);

        label = new Label();
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        getChildren().addAll(background, fill, label);
        StackPane.setAlignment(label, javafx.geometry.Pos.CENTER);
        setPadding(new Insets(2));

        update(0, 1);
    }

    /**
     * Aggiorna la barra con i valori correnti.
     * @param current valore corrente
     * @param max     valore massimo
     */
    public void update(int current, int max) {
        double ratio = max > 0 ? Math.max(0, Math.min(1.0, (double) current / max)) : 0;
        fill.setWidth(background.getWidth() * ratio);
        label.setText(String.format("%s: %d/%d", statName, current, max));
    }

    /**
     * Aggiorna la barra con una percentuale (0.0 - 1.0) e testo personalizzato.
     */
    public void update(double ratio, String text) {
        ratio = Math.max(0, Math.min(1.0, ratio));
        fill.setWidth(background.getWidth() * ratio);
        label.setText(text);
    }
}
