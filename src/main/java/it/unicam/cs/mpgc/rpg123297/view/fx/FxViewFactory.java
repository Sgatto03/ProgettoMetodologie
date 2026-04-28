package it.unicam.cs.mpgc.rpg123297.view.fx;

import it.unicam.cs.mpgc.rpg123297.controller.GameController;
import it.unicam.cs.mpgc.rpg123297.view.GameView;
import it.unicam.cs.mpgc.rpg123297.view.ViewFactory;
import it.unicam.cs.mpgc.rpg123297.view.fx.screen.*;

/**
 * Implementazione JavaFX della {@link ViewFactory}.
 * Crea tutte le schermate concrete per la piattaforma desktop.
 */
public class FxViewFactory implements ViewFactory {

    @Override
    public GameView createMainMenuView(GameController controller,
            Runnable onNewGame, Runnable onLoadGame,
            Runnable onExit) {
        return new MainMenuScreen(controller, onNewGame, onLoadGame, onExit);
    }

    @Override
    public GameView createCharacterCreationView(GameController controller,
            Runnable onCharacterCreated) {
        return new CharacterCreationScreen(controller, onCharacterCreated);
    }

    @Override
    public GameView createHubView(GameController controller,
            Runnable onArena, Runnable onShop,
            Runnable onSave, Runnable onMainMenu) {
        return new HubScreen(controller, onArena, onShop, onSave, onMainMenu);
    }

    @Override
    public GameView createArenaSelectionView(GameController controller,
            Runnable onBack) {
        return new ArenaSelectionScreen(controller, onBack);
    }

    @Override
    public GameView createCombatView(GameController controller,
            Runnable onCombatEnd) {
        return new CombatScreen(controller, onCombatEnd);
    }

    @Override
    public GameView createShopView(GameController controller, int tier,
            Runnable onBack) {
        return new ShopScreen(controller, tier, onBack);
    }

    @Override
    public GameView createGameOverView(GameController controller,
            boolean victory, Runnable onRestart,
            Runnable onMainMenu) {
        return new GameOverScreen(victory, onRestart, onMainMenu);
    }
}
