package it.unicam.cs.mpgc.rpg123297.view.fx;

import it.unicam.cs.mpgc.rpg123297.controller.GameController;
import it.unicam.cs.mpgc.rpg123297.event.EventType;
import it.unicam.cs.mpgc.rpg123297.event.GameEventBus;
import it.unicam.cs.mpgc.rpg123297.model.arena.Arena;
import it.unicam.cs.mpgc.rpg123297.model.character.CharacterStats;
import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;
import it.unicam.cs.mpgc.rpg123297.view.GameView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

/**
 * Entry point JavaFX dell'applicazione.
 * Gestisce la navigazione tra le schermate e il ciclo di vita dell'app.
 * Usa {@link FxViewFactory} per creare le viste concrete.
 */
public class FxGameApp extends Application {

    private Stage primaryStage;
    private GameController controller;
    private FxViewFactory viewFactory;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.controller = new GameController();
        this.viewFactory = new FxViewFactory();

        // Sottoscrizioni agli eventi per la navigazione
        GameEventBus bus = GameEventBus.getInstance();
        bus.subscribe(EventType.COMBAT_STARTED, event ->
                Platform.runLater(this::showCombatScreen));

        primaryStage.setTitle("Arena of Glory");
        primaryStage.setWidth(960);
        primaryStage.setHeight(700);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setOnCloseRequest(e -> {
            GameEventBus.resetInstance();
            Platform.exit();
        });

        showMainMenu();
        primaryStage.show();
    }

    /** Mostra la schermata del menu principale. */
    private void showMainMenu() {
        GameView view = viewFactory.createMainMenuView(controller,
                this::showCharacterCreation,
                this::showLoadDialog,
                () -> Platform.exit());
        setView(view);
    }

    /** Mostra la schermata di creazione del personaggio. */
    private void showCharacterCreation() {
        GameView view = viewFactory.createCharacterCreationView(controller,
                this::showHub);
        setView(view);
    }

    /** Mostra l'hub centrale. */
    private void showHub() {
        GameView view = viewFactory.createHubView(controller,
                this::showArenaSelection,
                this::showShopSelection,
                this::showSaveDialog,
                this::showMainMenu);
        setView(view);
    }

    /** Mostra la selezione dell'arena. */
    private void showArenaSelection() {
        GameView view = viewFactory.createArenaSelectionView(controller, this::showHub);
        // Sovrascriviamo la navigazione dei pulsanti sfida per andare al combat
        setView(view);
    }

    /** Mostra la schermata di combattimento. */
    private void showCombatScreen() {
        GameView view = viewFactory.createCombatView(controller, () -> {
            Gladiator g = controller.getGladiator();
            if (g.isAlive()) {
                // Traccia vittorie nell'Arena degli Dei (tier 5)
                if (controller.getCurrentCombatTier() == 5) {
                    g.addDivineVictory();
                }

                // Se ha punti statistica da assegnare, mostra il dialogo
                showStatAllocation();

                // Controlla se ha raggiunto la libertà
                if (g.hasEarnedFreedom()) {
                    showFreedomScreen();
                } else {
                    showHub();
                }
            } else {
                showGameOver(false);
            }
        });
        setView(view);
    }

    /** Mostra il dialogo per scegliere in quale arena fare acquisti. */
    private void showShopSelection() {
        List<Arena> accessible = controller.getAccessibleArenas();
        if (accessible.isEmpty()) {
            showAlert("Nessun negozio disponibile.");
            return;
        }

        // Se c'è una sola arena, vai direttamente
        if (accessible.size() == 1) {
            showShop(accessible.get(0).getTier().getTier());
            return;
        }

        ChoiceDialog<Arena> dialog = new ChoiceDialog<>(accessible.get(0), accessible);
        dialog.setTitle("Scegli Negozio");
        dialog.setHeaderText("A quale negozio dell'arena vuoi andare?");
        dialog.setContentText("Arena:");
        dialog.showAndWait().ifPresent(arena ->
                showShop(arena.getTier().getTier()));
    }

    /** Mostra la schermata del negozio. */
    private void showShop(int tier) {
        GameView view = viewFactory.createShopView(controller, tier, this::showHub);
        setView(view);
    }

    /** Mostra il dialogo di salvataggio. */
    private void showSaveDialog() {
        TextInputDialog dialog = new TextInputDialog("salvataggio_1");
        dialog.setTitle("Salva Partita");
        dialog.setHeaderText("Inserisci il nome del salvataggio:");
        dialog.setContentText("Nome:");
        dialog.showAndWait().ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                boolean success = controller.saveGame(name.trim());
                showAlert(success ? "Partita salvata con successo!"
                        : "Errore durante il salvataggio.");
            }
        });
    }

    /** Mostra il dialogo di caricamento. */
    private void showLoadDialog() {
        List<String> saves = controller.listSaves();
        if (saves.isEmpty()) {
            showAlert("Nessun salvataggio trovato.");
            return;
        }

        ChoiceDialog<String> dialog = new ChoiceDialog<>(saves.get(0), saves);
        dialog.setTitle("Carica Partita");
        dialog.setHeaderText("Seleziona il salvataggio da caricare:");
        dialog.setContentText("Salvataggio:");
        dialog.showAndWait().ifPresent(name -> {
            boolean success = controller.loadGame(name);
            if (success) {
                showHub();
            } else {
                showAlert("Errore durante il caricamento.");
            }
        });
    }

    /** Mostra la schermata di game over. */
    private void showGameOver(boolean victory) {
        GameView view = viewFactory.createGameOverView(controller, victory,
                this::showCharacterCreation,
                this::showMainMenu);
        setView(view);
    }

    /** Imposta la vista corrente sulla scena. */
    private void setView(GameView view) {
        Scene scene = new Scene(view.getRoot(),
                primaryStage.getWidth(), primaryStage.getHeight());
        // Stile globale della scena
        scene.setFill(javafx.scene.paint.Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle(view.getTitle());
    }

    /** Mostra un alert informativo. */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Arena of Glory");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Mostra il dialogo per assegnare i punti statistica dopo un level-up.
     * Il dialogo si ripete finché ci sono punti da assegnare.
     */
    private void showStatAllocation() {
        Gladiator g = controller.getGladiator();
        while (g.getUnspentStatPoints() > 0) {
            CharacterStats stats = g.getStats();
            String header = String.format(
                    "⬆️ Level Up! Hai %d punto/i statistica da assegnare.\n\n" +
                    "Statistiche attuali:\n" +
                    "  💪 Forza: %d    (danno attacco)\n" +
                    "  🏃 Agilità: %d    (schivata, iniziativa)\n" +
                    "  🛡️ Resistenza: %d    (HP massimi, difesa)\n" +
                    "  🎯 Destrezza: %d    (colpo critico)\n" +
                    "  ✨ Carisma: %d    (fama, sconti negozio)",
                    g.getUnspentStatPoints(),
                    stats.getStrength(), stats.getAgility(), stats.getEndurance(),
                    stats.getDexterity(), stats.getCharisma());

            ChoiceDialog<CharacterStats.StatType> dialog = new ChoiceDialog<>(
                    CharacterStats.StatType.STRENGTH,
                    CharacterStats.StatType.values());
            dialog.setTitle("Assegna Punti Statistica");
            dialog.setHeaderText(header);
            dialog.setContentText("Scegli la statistica da aumentare:");

            var result = dialog.showAndWait();
            if (result.isPresent()) {
                g.spendStatPoint(result.get());
            } else {
                // Se chiude il dialogo, i punti restano per dopo
                break;
            }
        }
    }

    /**
     * Mostra la schermata epilogo di liberazione del gladiatore.
     * Raggiunta dopo 10 vittorie nell'Arena degli Dei.
     */
    private void showFreedomScreen() {
        Gladiator g = controller.getGladiator();

        javafx.scene.layout.VBox root = new javafx.scene.layout.VBox(20);
        root.setAlignment(javafx.geometry.Pos.CENTER);
        root.setPadding(new javafx.geometry.Insets(50));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0d0d0d, #1a0a00, #332200, #1a0a00, #0d0d0d);");

        // Titolo
        Label titleLabel = new Label("🏛️ LIBERTÀ! 🏛️");
        titleLabel.setStyle(
                "-fx-text-fill: #ffd700; -fx-font-size: 48px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Georgia';");
        titleLabel.setEffect(new javafx.scene.effect.DropShadow(30, javafx.scene.paint.Color.web("#ff8800")));

        // Testo narrativo
        Label storyLabel = new Label(String.format(
                "Dopo %d vittorie nell'Arena degli Dei,\n" +
                "l'Imperatore in persona si alza dal suo trono.\n\n" +
                "\"Mai prima d'ora un gladiatore ha mostrato\n" +
                "tale valore e tale coraggio.\"\n\n" +
                "Con un gesto solenne, ti porge la Rudis —\n" +
                "la spada di legno che simboleggia la tua libertà.\n\n" +
                "\"%s, da oggi non sei più uno schiavo.\n" +
                "Sei un uomo libero. Sei una leggenda.\"",
                g.getDivineVictories(), g.getName()));
        storyLabel.setStyle(
                "-fx-text-fill: #ccccaa; -fx-font-size: 16px; " +
                "-fx-font-family: 'Georgia'; -fx-text-alignment: center;");
        storyLabel.setWrapText(true);
        storyLabel.setMaxWidth(600);

        // Statistiche finali
        Label statsLabel = new Label(String.format(
                "⭐ Livello: %d   |   🏆 Fama: %d   |   💰 Oro: %d   |   🗡️ Classe: %s",
                g.getLevel(), g.getFame(), g.getGold(),
                g.getGladiatorClass().getDisplayName()));
        statsLabel.setStyle("-fx-text-fill: #ffd700; -fx-font-size: 14px;");

        // Separatore
        Label separator = new Label("— FINE —");
        separator.setStyle("-fx-text-fill: #664400; -fx-font-size: 18px; -fx-font-weight: bold; " +
                "-fx-font-family: 'Georgia';");

        // Pulsanti
        javafx.scene.layout.HBox buttons = new javafx.scene.layout.HBox(20);
        buttons.setAlignment(javafx.geometry.Pos.CENTER);

        Button newGameBtn = new Button("🗡️ Nuova Partita");
        newGameBtn.setStyle(
                "-fx-background-color: #664400; -fx-text-fill: #ffd700; " +
                "-fx-font-size: 14px; -fx-font-weight: bold; " +
                "-fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 10 25;");
        newGameBtn.setOnAction(e -> showCharacterCreation());

        Button menuBtn = new Button("🚪 Menu Principale");
        menuBtn.setStyle(
                "-fx-background-color: #1a1a1a; -fx-text-fill: #aaaaaa; " +
                "-fx-font-size: 14px; -fx-background-radius: 8; " +
                "-fx-border-color: #444444; -fx-cursor: hand; -fx-padding: 10 25;");
        menuBtn.setOnAction(e -> showMainMenu());

        buttons.getChildren().addAll(newGameBtn, menuBtn);

        root.getChildren().addAll(titleLabel, storyLabel, statsLabel, separator, buttons);

        Scene scene = new Scene(root, primaryStage.getWidth(), primaryStage.getHeight());
        scene.setFill(javafx.scene.paint.Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Arena of Glory — Libertà!");
    }
}
