package it.unicam.cs.mpgc.rpg123297.controller;

import it.unicam.cs.mpgc.rpg123297.event.EventType;
import it.unicam.cs.mpgc.rpg123297.event.GameEventBus;
import it.unicam.cs.mpgc.rpg123297.model.character.Enemy;
import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;
import it.unicam.cs.mpgc.rpg123297.model.combat.*;

/**
 * Controller per la gestione del flusso di combattimento.
 * Si pone tra la view e il {@link CombatEngine}, traducendo
 * le interazioni dell'utente in azioni di combattimento.
 */
public class CombatController {

    private final CombatEngine engine;
    private final GameEventBus eventBus;

    // Azioni predefinite disponibili al giocatore
    private final AttackAction attackAction = new AttackAction();
    private final DefendAction defendAction = new DefendAction();
    private final SpecialAction specialAction = new SpecialAction();
    private final UseItemAction useItemAction = new UseItemAction();

    public CombatController(Gladiator gladiator, Enemy enemy) {
        this.engine = new CombatEngine(gladiator, enemy);
        this.eventBus = GameEventBus.getInstance();
    }

    /** Inizializza il combattimento. */
    public void startCombat() {
        engine.initCombat();

        // Se il nemico ha l'iniziativa, gioca il suo turno immediatamente
        if (!engine.isPlayerTurn() && !engine.isCombatOver()) {
            engine.executeEnemyTurn();
        }
    }

    /** Il giocatore sceglie di attaccare. */
    public void playerAttack() {
        if (!canPlayerAct())
            return;
        engine.executePlayerTurn(attackAction);
        afterPlayerTurn();
    }

    /** Il giocatore sceglie di difendersi. */
    public void playerDefend() {
        if (!canPlayerAct())
            return;
        engine.executePlayerTurn(defendAction);
        afterPlayerTurn();
    }

    /** Il giocatore usa l'abilità speciale. */
    public void playerSpecial() {
        if (!canPlayerAct())
            return;
        if (!specialAction.isAvailable(engine.getGladiator()))
            return;
        engine.executePlayerTurn(specialAction);
        afterPlayerTurn();
    }

    /** Il giocatore usa un consumabile. */
    public void playerUseItem(it.unicam.cs.mpgc.rpg123297.model.item.Consumable item) {
        if (!canPlayerAct())
            return;
        useItemAction.setSelectedItem(item);
        engine.executePlayerTurn(useItemAction);
        afterPlayerTurn();
    }

    /** Gestisce la fine del turno del giocatore e il turno del nemico. */
    private void afterPlayerTurn() {
        eventBus.publish(EventType.COMBAT_ACTION_PERFORMED, engine);

        if (!engine.isCombatOver()) {
            engine.executeEnemyTurn();
            eventBus.publish(EventType.COMBAT_ACTION_PERFORMED, engine);
        }

        if (engine.isCombatOver()) {
            eventBus.publish(EventType.COMBAT_ENDED, engine.getResult());
        }
    }

    /** Verifica se il giocatore può agire. */
    private boolean canPlayerAct() {
        return !engine.isCombatOver() && engine.isPlayerTurn();
    }

    // --- Getters per la view ---

    public CombatEngine getEngine() {
        return engine;
    }

    public Gladiator getGladiator() {
        return engine.getGladiator();
    }

    public Enemy getEnemy() {
        return engine.getEnemy();
    }

    public CombatLog getLog() {
        return engine.getLog();
    }

    public boolean isCombatOver() {
        return engine.isCombatOver();
    }

    public CombatResult getResult() {
        return engine.getResult();
    }

    public boolean isSpecialAvailable() {
        return specialAction.isAvailable(engine.getGladiator());
    }

    public boolean hasConsumables() {
        return useItemAction.isAvailable(engine.getGladiator());
    }
}
