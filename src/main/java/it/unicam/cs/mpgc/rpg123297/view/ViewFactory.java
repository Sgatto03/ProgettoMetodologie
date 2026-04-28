package it.unicam.cs.mpgc.rpg123297.view;

import it.unicam.cs.mpgc.rpg123297.controller.GameController;

/**
 * Interfaccia Abstract Factory per la creazione delle viste del gioco.
 * Permette di cambiare l'intera interfaccia grafica sostituendo
 * la factory (es. da JavaFX desktop a Android o web).
 */
public interface ViewFactory {

        /** Crea la schermata del menu principale. */
        GameView createMainMenuView(GameController controller, Runnable onNewGame,
                        Runnable onLoadGame, Runnable onExit);

        /** Crea la schermata di creazione del personaggio. */
        GameView createCharacterCreationView(GameController controller,
                        Runnable onCharacterCreated);

        /** Crea la schermata hub centrale. */
        GameView createHubView(GameController controller,
                        Runnable onArena, Runnable onShop,
                        Runnable onSave, Runnable onMainMenu);

        /** Crea la schermata di selezione dell'arena. */
        GameView createArenaSelectionView(GameController controller,
                        Runnable onBack);

        /** Crea la schermata di combattimento. */
        GameView createCombatView(GameController controller,
                        Runnable onCombatEnd);

        /** Crea la schermata del negozio. */
        GameView createShopView(GameController controller, int tier,
                        Runnable onBack);

        /** Crea la schermata di game over. */
        GameView createGameOverView(GameController controller,
                        boolean victory, Runnable onRestart,
                        Runnable onMainMenu);
}
