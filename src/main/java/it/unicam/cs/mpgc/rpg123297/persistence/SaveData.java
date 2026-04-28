package it.unicam.cs.mpgc.rpg123297.persistence;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object (DTO) per il salvataggio/caricamento del gioco.
 * Contiene una rappresentazione serializzabile dello stato completo del gioco.
 * È indipendente dalle classi di dominio per separare la persistenza dal
 * modello.
 */
public class SaveData {

    // --- Informazioni salvataggio ---
    private String saveName;
    private String saveDate;

    // --- Gladiatore ---
    private String gladiatorName;
    private String gladiatorClass; // nome dell'enum GladiatorClass
    private int level;
    private int experience;
    private int gold;
    private int fame;
    private int currentHp;
    private int unspentStatPoints;
    private int divineVictories;

    // --- Statistiche ---
    private int strength;
    private int agility;
    private int endurance;
    private int dexterity;
    private int charisma;

    // --- Equipaggiamento (id oggetto per slot) ---
    private Map<String, String> equippedItems; // slot name -> item id
    private List<SavedItem> equippedItemData; // dati completi degli oggetti equipaggiati

    // --- Inventario (lista di id oggetti) ---
    private List<SavedItem> inventoryItems;

    public SaveData() {
        this.equippedItems = new HashMap<>();
        this.saveDate = LocalDateTime.now().toString();
    }

    /**
     * Rappresentazione serializzabile di un oggetto nell'inventario.
     */
    public static class SavedItem {
        private String id;
        private String type; // "weapon", "armor", "consumable"
        private String rarity; // nome dell'enum ItemRarity
        private String name;

        public SavedItem() {
        }

        public SavedItem(String id, String type, String rarity, String name) {
            this.id = id;
            this.type = type;
            this.rarity = rarity;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRarity() {
            return rarity;
        }

        public void setRarity(String rarity) {
            this.rarity = rarity;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    // --- Getters e Setters ---

    public String getSaveName() {
        return saveName;
    }

    public void setSaveName(String saveName) {
        this.saveName = saveName;
    }

    public String getSaveDate() {
        return saveDate;
    }

    public void setSaveDate(String saveDate) {
        this.saveDate = saveDate;
    }

    public String getGladiatorName() {
        return gladiatorName;
    }

    public void setGladiatorName(String gladiatorName) {
        this.gladiatorName = gladiatorName;
    }

    public String getGladiatorClass() {
        return gladiatorClass;
    }

    public void setGladiatorClass(String gladiatorClass) {
        this.gladiatorClass = gladiatorClass;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getFame() {
        return fame;
    }

    public void setFame(int fame) {
        this.fame = fame;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public int getUnspentStatPoints() {
        return unspentStatPoints;
    }

    public void setUnspentStatPoints(int unspentStatPoints) {
        this.unspentStatPoints = unspentStatPoints;
    }

    public int getDivineVictories() {
        return divineVictories;
    }

    public void setDivineVictories(int divineVictories) {
        this.divineVictories = divineVictories;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getEndurance() {
        return endurance;
    }

    public void setEndurance(int endurance) {
        this.endurance = endurance;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public Map<String, String> getEquippedItems() {
        return equippedItems;
    }

    public void setEquippedItems(Map<String, String> equippedItems) {
        this.equippedItems = equippedItems;
    }

    public List<SavedItem> getEquippedItemData() {
        return equippedItemData;
    }

    public void setEquippedItemData(List<SavedItem> equippedItemData) {
        this.equippedItemData = equippedItemData;
    }

    public List<SavedItem> getInventoryItems() {
        return inventoryItems;
    }

    public void setInventoryItems(List<SavedItem> inventoryItems) {
        this.inventoryItems = inventoryItems;
    }
}
