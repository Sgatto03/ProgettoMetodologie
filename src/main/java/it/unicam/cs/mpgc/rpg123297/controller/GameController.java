package it.unicam.cs.mpgc.rpg123297.controller;

import it.unicam.cs.mpgc.rpg123297.event.EventType;
import it.unicam.cs.mpgc.rpg123297.event.GameEventBus;
import it.unicam.cs.mpgc.rpg123297.model.arena.Arena;
import it.unicam.cs.mpgc.rpg123297.model.arena.ArenaManager;
import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;
import it.unicam.cs.mpgc.rpg123297.model.character.GladiatorClass;
import it.unicam.cs.mpgc.rpg123297.model.item.EquipmentSlot;
import it.unicam.cs.mpgc.rpg123297.model.item.Item;
import it.unicam.cs.mpgc.rpg123297.model.progression.FameRank;
import it.unicam.cs.mpgc.rpg123297.model.progression.ProgressionManager;
import it.unicam.cs.mpgc.rpg123297.model.shop.ArenaShop;
import it.unicam.cs.mpgc.rpg123297.persistence.GameDataMapper;
import it.unicam.cs.mpgc.rpg123297.persistence.GameRepository;
import it.unicam.cs.mpgc.rpg123297.persistence.JsonGameRepository;
import it.unicam.cs.mpgc.rpg123297.persistence.SaveData;

import java.util.List;
import java.util.Optional;

/**
 * Controller principale del gioco. Orchestrata tutte le componenti:
 * personaggio, arene, progressione, negozio e persistenza.
 * Funge da punto unico di accesso per la view (Facade pattern).
 */
public class GameController {

    private Gladiator gladiator;
    private final ArenaManager arenaManager;
    private final ProgressionManager progressionManager;
    private final GameRepository repository;
    private final GameEventBus eventBus;

    private CombatController combatController;
    private ShopController shopController;
    private int currentCombatTier;

    public GameController() {
        this.arenaManager = new ArenaManager();
        this.progressionManager = new ProgressionManager();
        this.repository = new JsonGameRepository();
        this.eventBus = GameEventBus.getInstance();
    }

    // ==================== CREAZIONE PERSONAGGIO ====================

    /**
     * Crea un nuovo gladiatore e inizia una nuova partita.
     */
    public void createGladiator(String name, GladiatorClass gladiatorClass) {
        this.gladiator = new Gladiator(name, gladiatorClass);
        eventBus.publish(EventType.GLADIATOR_CREATED, gladiator);
    }

    // ==================== ACCESSORI ====================

    public Gladiator getGladiator() {
        return gladiator;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public ProgressionManager getProgressionManager() {
        return progressionManager;
    }

    public FameRank getCurrentFameRank() {
        return progressionManager.getCurrentRank(gladiator);
    }

    public List<Arena> getAccessibleArenas() {
        return arenaManager.getAccessibleArenas(gladiator.getFame());
    }

    public Arena getNextLockedArena() {
        return arenaManager.getNextLockedArena(gladiator.getFame());
    }

    // ==================== COMBATTIMENTO ====================

    public CombatController getCombatController() {
        return combatController;
    }

    /** Restituisce il tier dell'arena del combattimento corrente. */
    public int getCurrentCombatTier() {
        return currentCombatTier;
    }

    /**
     * Inizia un nuovo combattimento per la sfida specificata.
     */
    public CombatController startCombat(
            it.unicam.cs.mpgc.rpg123297.model.arena.ArenaChallenge challenge) {
        var enemy = challenge.generateEnemy();
        this.currentCombatTier = challenge.getTier();
        combatController = new CombatController(gladiator, enemy);
        combatController.startCombat();
        eventBus.publish(EventType.COMBAT_STARTED, combatController);
        return combatController;
    }

    // ==================== NEGOZIO ====================

    public ShopController getShopController() {
        return shopController;
    }

    /**
     * Apre il negozio dell'arena specificata.
     */
    public ShopController openShop(int tier) {
        ArenaShop shop = new ArenaShop(tier);
        shopController = new ShopController(shop, gladiator);
        return shopController;
    }

    // ==================== EQUIPAGGIAMENTO ====================

    /**
     * Equipaggia un oggetto dall'inventario.
     */
    public void equipItem(Item item, EquipmentSlot slot) {
        Optional<Item> previous = gladiator.getEquipment().equip(slot, item);
        gladiator.getInventory().removeItem(item);
        previous.ifPresent(prev -> gladiator.getInventory().addItem(prev));
        eventBus.publish(EventType.ITEM_EQUIPPED, item);
    }

    /**
     * Rimuove un oggetto equipaggiato e lo rimette nell'inventario.
     */
    public void unequipItem(EquipmentSlot slot) {
        gladiator.getEquipment().unequip(slot).ifPresent(item -> {
            gladiator.getInventory().addItem(item);
            eventBus.publish(EventType.ITEM_UNEQUIPPED, item);
        });
    }

    // ==================== PERSISTENZA ====================

    /**
     * Salva la partita corrente.
     */
    public boolean saveGame(String slotName) {
        if (gladiator == null)
            return false;
        SaveData data = GameDataMapper.toSaveData(gladiator, slotName);
        boolean success = repository.save(data, slotName);
        if (success) {
            eventBus.publish(EventType.GAME_SAVED, slotName);
        }
        return success;
    }

    /**
     * Carica una partita salvata.
     */
    public boolean loadGame(String slotName) {
        Optional<SaveData> data = repository.load(slotName);
        if (data.isPresent()) {
            this.gladiator = GameDataMapper.fromSaveData(data.get());
            eventBus.publish(EventType.GAME_LOADED, gladiator);
            return true;
        }
        return false;
    }

    /**
     * Elimina un salvataggio.
     */
    public boolean deleteSave(String slotName) {
        return repository.delete(slotName);
    }

    /**
     * Restituisce la lista dei salvataggi disponibili.
     */
    public List<String> listSaves() {
        return repository.listSaves();
    }
}
