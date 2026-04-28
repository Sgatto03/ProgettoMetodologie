package it.unicam.cs.mpgc.rpg123297.view.fx.screen;

import it.unicam.cs.mpgc.rpg123297.controller.GameController;
import it.unicam.cs.mpgc.rpg123297.model.character.CharacterStats;
import it.unicam.cs.mpgc.rpg123297.model.character.GladiatorClass;
import it.unicam.cs.mpgc.rpg123297.view.GameView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;

/**
 * Schermata di creazione del personaggio.
 * Il giocatore sceglie il nome e la classe del gladiatore.
 */
public class CharacterCreationScreen implements GameView {

    private final VBox root;
    private GladiatorClass selectedClass;
    private final Label classInfoLabel;
    private final Label statsInfoLabel;
    private final Label specialInfoLabel;

    public CharacterCreationScreen(GameController controller, Runnable onCharacterCreated) {
        this.selectedClass = GladiatorClass.MURMILLO;

        root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0d0d0d, #110a00, #0d0d0d);");

        // Titolo
        Label title = new Label("⚔️ Crea il tuo Gladiatore");
        title.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 28px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Georgia';");

        // Nome
        Label nameLabel = new Label("Nome del Gladiatore:");
        nameLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 14px;");
        TextField nameField = new TextField();
        nameField.setId("field-name");
        nameField.setPromptText("Inserisci il nome...");
        nameField.setMaxWidth(300);
        nameField.setStyle(
                "-fx-background-color: #1a1a1a; -fx-text-fill: #ffffff; " +
                "-fx-border-color: #664400; -fx-border-radius: 4; " +
                "-fx-background-radius: 4; -fx-font-size: 14px; -fx-prompt-text-fill: #666666;");

        // Selezione classe
        Label classLabel = new Label("Scegli la tua Classe:");
        classLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 14px;");

        HBox classButtons = new HBox(10);
        classButtons.setAlignment(Pos.CENTER);
        ToggleGroup classGroup = new ToggleGroup();

        for (GladiatorClass gc : GladiatorClass.values()) {
            ToggleButton btn = new ToggleButton(gc.getDisplayName());
            btn.setToggleGroup(classGroup);
            btn.setUserData(gc);
            btn.setPrefWidth(130);
            btn.setPrefHeight(40);
            btn.setStyle(getClassButtonStyle(false));

            btn.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                btn.setStyle(getClassButtonStyle(isSelected));
                if (isSelected) {
                    selectedClass = gc;
                    updateClassInfo();
                }
            });

            if (gc == GladiatorClass.MURMILLO) {
                btn.setSelected(true);
            }

            classButtons.getChildren().add(btn);
        }

        // Info classe selezionata
        VBox infoBox = new VBox(8);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPadding(new Insets(15));
        infoBox.setMaxWidth(500);
        infoBox.setStyle("-fx-background-color: #1a1a1a; -fx-background-radius: 8; " +
                "-fx-border-color: #333333; -fx-border-radius: 8;");

        classInfoLabel = new Label();
        classInfoLabel.setWrapText(true);
        classInfoLabel.setStyle("-fx-text-fill: #cccccc; -fx-font-size: 13px;");

        statsInfoLabel = new Label();
        statsInfoLabel.setStyle("-fx-text-fill: #44aaff; -fx-font-size: 13px;");

        specialInfoLabel = new Label();
        specialInfoLabel.setStyle("-fx-text-fill: #ff8844; -fx-font-size: 13px;");

        infoBox.getChildren().addAll(classInfoLabel, statsInfoLabel, specialInfoLabel);
        updateClassInfo();

        // Pulsante conferma
        Button confirmBtn = new Button("🗡️ Inizia l'Avventura!");
        confirmBtn.setId("btn-confirm-creation");
        confirmBtn.setPrefWidth(250);
        confirmBtn.setPrefHeight(45);
        confirmBtn.setStyle(
                "-fx-background-color: #664400; -fx-text-fill: #ffd700; " +
                "-fx-font-size: 16px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-border-radius: 8; " +
                "-fx-border-color: #ffd700; -fx-border-width: 2; -fx-cursor: hand;");

        confirmBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                nameField.setStyle(nameField.getStyle() + "-fx-border-color: #ff0000;");
                return;
            }
            controller.createGladiator(name, selectedClass);
            onCharacterCreated.run();
        });

        root.getChildren().addAll(title, nameLabel, nameField,
                classLabel, classButtons, infoBox, confirmBtn);
    }

    private void updateClassInfo() {
        if (selectedClass == null) return;
        classInfoLabel.setText(selectedClass.getDescription());
        CharacterStats stats = selectedClass.getBaseStats();
        statsInfoLabel.setText(String.format(
                "📊 STR:%d  AGI:%d  END:%d  DEX:%d  CHA:%d",
                stats.getStrength(), stats.getAgility(), stats.getEndurance(),
                stats.getDexterity(), stats.getCharisma()));
        specialInfoLabel.setText(String.format(
                "✨ %s: %s", selectedClass.getSpecialAbilityName(),
                selectedClass.getSpecialAbilityDescription()));
    }

    private String getClassButtonStyle(boolean selected) {
        if (selected) {
            return "-fx-background-color: #332200; -fx-text-fill: #ffd700; " +
                    "-fx-font-size: 13px; -fx-font-weight: bold; " +
                    "-fx-background-radius: 6; -fx-border-radius: 6; " +
                    "-fx-border-color: #ffd700; -fx-border-width: 2;";
        }
        return "-fx-background-color: #1a1a1a; -fx-text-fill: #aaaaaa; " +
                "-fx-font-size: 13px; -fx-background-radius: 6; " +
                "-fx-border-radius: 6; -fx-border-color: #444444; -fx-border-width: 1;";
    }

    @Override
    public Parent getRoot() { return root; }

    @Override
    public void refresh() { updateClassInfo(); }

    @Override
    public String getTitle() { return "Arena of Glory — Creazione Personaggio"; }
}
