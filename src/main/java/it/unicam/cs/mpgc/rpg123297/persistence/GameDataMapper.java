package it.unicam.cs.mpgc.rpg123297.persistence;

import it.unicam.cs.mpgc.rpg123297.model.character.Gladiator;
import it.unicam.cs.mpgc.rpg123297.model.character.GladiatorClass;
import it.unicam.cs.mpgc.rpg123297.model.item.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Mapper che converte tra gli oggetti di dominio e i DTO di persistenza.
 * Garantisce la separazione tra il modello e la struttura dei dati salvati.
 */
public class GameDataMapper {

    /**
     * Converte un Gladiator in un SaveData pronto per la serializzazione.
     */
    public static SaveData toSaveData(Gladiator gladiator, String saveName) {
        SaveData data = new SaveData();
        data.setSaveName(saveName);
        data.setGladiatorName(gladiator.getName());
        data.setGladiatorClass(gladiator.getGladiatorClass().name());
        data.setLevel(gladiator.getLevel());
        data.setExperience(gladiator.getExperience());
        data.setGold(gladiator.getGold());
        data.setFame(gladiator.getFame());
        data.setCurrentHp(gladiator.getCurrentHp());
        data.setUnspentStatPoints(gladiator.getUnspentStatPoints());
        data.setDivineVictories(gladiator.getDivineVictories());

        // Statistiche
        data.setStrength(gladiator.getStats().getStrength());
        data.setAgility(gladiator.getStats().getAgility());
        data.setEndurance(gladiator.getStats().getEndurance());
        data.setDexterity(gladiator.getStats().getDexterity());
        data.setCharisma(gladiator.getStats().getCharisma());

        // Equipaggiamento — salva sia lo slot che i dati completi dell'oggetto
        Map<EquipmentSlot, Item> equipped = gladiator.getEquipment().getAllEquipped();
        List<SaveData.SavedItem> equippedSavedItems = new ArrayList<>();
        for (var entry : equipped.entrySet()) {
            Item item = entry.getValue();
            data.getEquippedItems().put(entry.getKey().name(), item.getId());
            // Salva anche i dati completi dell'oggetto equipaggiato
            equippedSavedItems.add(toSavedItem(item));
        }
        data.setEquippedItemData(equippedSavedItems);

        // Inventario
        List<SaveData.SavedItem> savedItems = new ArrayList<>();
        for (Item item : gladiator.getInventory().getAllItems()) {
            savedItems.add(toSavedItem(item));
        }
        data.setInventoryItems(savedItems);

        return data;
    }

    /**
     * Converte un Item in un SavedItem serializzabile.
     */
    private static SaveData.SavedItem toSavedItem(Item item) {
        String type = item instanceof Weapon ? "weapon"
                : item instanceof Armor ? "armor"
                        : item instanceof Consumable ? "consumable"
                                : "unknown";
        return new SaveData.SavedItem(
                item.getId(), type, item.getRarity().name(), item.getName());
    }

    /**
     * Ricostruisce un Gladiator da un SaveData caricato.
     */
    public static Gladiator fromSaveData(SaveData data) {
        GladiatorClass gc = GladiatorClass.valueOf(data.getGladiatorClass());
        Gladiator gladiator = new Gladiator(data.getGladiatorName(), gc);

        // Ripristina il livello e la progressione
        gladiator.setLevel(data.getLevel());
        gladiator.setExperience(data.getExperience());
        gladiator.setGold(data.getGold());
        gladiator.setFame(data.getFame());
        gladiator.setUnspentStatPoints(data.getUnspentStatPoints());
        gladiator.setDivineVictories(data.getDivineVictories());

        // Ripristina le statistiche
        gladiator.getStats().setStrength(data.getStrength());
        gladiator.getStats().setAgility(data.getAgility());
        gladiator.getStats().setEndurance(data.getEndurance());
        gladiator.getStats().setDexterity(data.getDexterity());
        gladiator.getStats().setCharisma(data.getCharisma());

        // Ripristina l'inventario
        if (data.getInventoryItems() != null) {
            for (SaveData.SavedItem si : data.getInventoryItems()) {
                Item item = recreateItem(si);
                if (item != null) {
                    gladiator.getInventory().addItem(item);
                }
            }
        }

        // Ripristina l'equipaggiamento — ricrea gli oggetti dai dati salvati
        if (data.getEquippedItems() != null && data.getEquippedItemData() != null) {
            // Mappa id → SavedItem per ricreare gli oggetti equipaggiati
            Map<String, SaveData.SavedItem> itemDataById = new java.util.HashMap<>();
            for (SaveData.SavedItem si : data.getEquippedItemData()) {
                itemDataById.put(si.getId(), si);
            }
            for (var entry : data.getEquippedItems().entrySet()) {
                EquipmentSlot slot = EquipmentSlot.valueOf(entry.getKey());
                String itemId = entry.getValue();
                SaveData.SavedItem savedItem = itemDataById.get(itemId);
                if (savedItem != null) {
                    Item item = recreateItem(savedItem);
                    if (item != null) {
                        gladiator.getEquipment().equip(slot, item);
                    }
                }
            }
        } else if (data.getEquippedItems() != null) {
            // Retrocompatibilità: vecchi salvataggi senza equippedItemData
            for (var entry : data.getEquippedItems().entrySet()) {
                EquipmentSlot slot = EquipmentSlot.valueOf(entry.getKey());
                String itemId = entry.getValue();
                gladiator.getInventory().findById(itemId).ifPresent(item -> {
                    gladiator.getEquipment().equip(slot, item);
                    gladiator.getInventory().removeItem(item);
                });
            }
        }

        return gladiator;
    }

    /**
     * Ricrea un oggetto dal suo SavedItem.
     */
    private static Item recreateItem(SaveData.SavedItem si) {
        ItemRarity rarity = ItemRarity.valueOf(si.getRarity());
        return switch (si.getId()) {
            // Armi
            case "wpn_gladius" -> ItemFactory.createGladius(rarity);
            case "wpn_spatha" -> ItemFactory.createSpatha(rarity);
            case "wpn_trident" -> ItemFactory.createTrident(rarity);
            case "wpn_waraxe" -> ItemFactory.createWarAxe(rarity);
            case "wpn_warhammer" -> ItemFactory.createWarHammer(rarity);
            case "wpn_dagger" -> ItemFactory.createDagger(rarity);
            case "wpn_scutum" -> ItemFactory.createScutum(rarity);
            // Armature
            case "arm_leather" -> ItemFactory.createLeatherArmor(rarity);
            case "arm_chainmail" -> ItemFactory.createChainMail(rarity);
            case "arm_plate" -> ItemFactory.createPlateArmor(rarity);
            case "arm_leatherhelm" -> ItemFactory.createLeatherHelm(rarity);
            case "arm_bronzehelm" -> ItemFactory.createBronzeHelm(rarity);
            case "arm_leathergreaves" -> ItemFactory.createLeatherGreaves(rarity);
            case "arm_irongreaves" -> ItemFactory.createIronGreaves(rarity);
            // Consumabili
            case "con_healthpot" -> ItemFactory.createHealthPotion(rarity);
            case "con_strelixir" -> ItemFactory.createStrengthElixir(rarity);
            case "con_ironskin" -> ItemFactory.createIronSkinPotion(rarity);
            default -> null;
        };
    }
}
