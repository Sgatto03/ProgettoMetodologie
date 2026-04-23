package it.unicam.cs.mpgc.rpg123297.model.item;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory per la creazione di armi, armature e consumabili.
 * Centralizza la logica di creazione e garantisce coerenza tra i tier.
 * L'aggiunta di nuovi oggetti avviene qui senza modificare il resto del codice.
 */
public class ItemFactory {

    private ItemFactory() {
        // impedisce l'istanziazione
    }

    // ==================== ARMI ====================

    public static Weapon createGladius(ItemRarity rarity) {
        return new Weapon("wpn_gladius", "Gladius", "La spada corta del gladiatore romano.",
                30, rarity, 8);
    }

    public static Weapon createSpatha(ItemRarity rarity) {
        return new Weapon("wpn_spatha", "Spatha", "Una spada lunga di origine celtica.",
                55, rarity, 12);
    }

    public static Weapon createTrident(ItemRarity rarity) {
        return new Weapon("wpn_trident", "Tridente", "Arma a tre punte del retiarius.",
                45, rarity, 10);
    }

    public static Weapon createWarAxe(ItemRarity rarity) {
        return new Weapon("wpn_waraxe", "Ascia da Guerra",
                "Un'ascia pesante che infligge danni devastanti.", 70, rarity, 15);
    }

    public static Weapon createWarHammer(ItemRarity rarity) {
        return new Weapon("wpn_warhammer", "Martello da Guerra",
                "Un martello massiccio capace di sfondare le armature.", 85, rarity, 18);
    }

    public static Weapon createDagger(ItemRarity rarity) {
        return new Weapon("wpn_dagger", "Pugnale", "Un'arma leggera e veloce.",
                15, rarity, 5);
    }

    public static Weapon createScutum(ItemRarity rarity) {
        return new Weapon("wpn_scutum", "Scutum",
                "Il grande scudo rettangolare romano. Offre protezione extra.",
                40, rarity, 2, EquipmentSlot.SHIELD);
    }

    // ==================== ARMATURE ====================

    public static Armor createLeatherArmor(ItemRarity rarity) {
        return new Armor("arm_leather", "Armatura di Cuoio",
                "Protezione leggera in pelle.", 25, rarity, 4, EquipmentSlot.CHEST);
    }

    public static Armor createChainMail(ItemRarity rarity) {
        return new Armor("arm_chainmail", "Maglia di Ferro",
                "Una maglia di anelli metallici intrecciati.", 50, rarity, 8, EquipmentSlot.CHEST);
    }

    public static Armor createPlateArmor(ItemRarity rarity) {
        return new Armor("arm_plate", "Armatura a Piastre",
                "Pesante armatura di piastre metalliche.", 80, rarity, 14, EquipmentSlot.CHEST);
    }

    public static Armor createLeatherHelm(ItemRarity rarity) {
        return new Armor("arm_leatherhelm", "Elmo di Cuoio",
                "Un semplice elmo in pelle.", 15, rarity, 2, EquipmentSlot.HELMET);
    }

    public static Armor createBronzeHelm(ItemRarity rarity) {
        return new Armor("arm_bronzehelm", "Elmo di Bronzo",
                "Un elmo robusto di bronzo.", 35, rarity, 5, EquipmentSlot.HELMET);
    }

    public static Armor createLeatherGreaves(ItemRarity rarity) {
        return new Armor("arm_leathergreaves", "Gambali di Cuoio",
                "Protezione leggera per le gambe.", 20, rarity, 3, EquipmentSlot.LEGS);
    }

    public static Armor createIronGreaves(ItemRarity rarity) {
        return new Armor("arm_irongreaves", "Gambali di Ferro",
                "Protezione robusta per le gambe.", 45, rarity, 6, EquipmentSlot.LEGS);
    }

    // ==================== CONSUMABILI ====================

    public static Consumable createHealthPotion(ItemRarity rarity) {
        return new Consumable("con_healthpot", "Pozione di Salute",
                "Ripristina punti vita.", 20, rarity,
                Consumable.EffectType.HEAL, 30);
    }

    public static Consumable createStrengthElixir(ItemRarity rarity) {
        return new Consumable("con_strelixir", "Elisir di Forza",
                "Aumenta temporaneamente la forza.", 35, rarity,
                Consumable.EffectType.DAMAGE_BOOST, 3);
    }

    public static Consumable createIronSkinPotion(ItemRarity rarity) {
        return new Consumable("con_ironskin", "Pozione Pellediferro",
                "Aumenta temporaneamente la resistenza.", 35, rarity,
                Consumable.EffectType.DEFENSE_BOOST, 3);
    }

    // ==================== METODI DI UTILITÀ PER TIER ====================

    /**
     * Restituisce una lista di armi appropriate per il tier specificato.
     * Usato da {@code EnemyFactory} e dal sistema negozio.
     */
    public static List<Weapon> getWeaponsForTier(int tier) {
        List<Weapon> weapons = new ArrayList<>();
        ItemRarity rarity = getRarityForTier(tier);

        switch (tier) {
            case 1 -> {
                weapons.add(createDagger(rarity));
                weapons.add(createGladius(rarity));
            }
            case 2 -> {
                weapons.add(createGladius(rarity));
                weapons.add(createTrident(rarity));
            }
            case 3 -> {
                weapons.add(createSpatha(rarity));
                weapons.add(createTrident(rarity));
                weapons.add(createWarAxe(rarity));
            }
            case 4 -> {
                weapons.add(createWarAxe(rarity));
                weapons.add(createWarHammer(rarity));
                weapons.add(createSpatha(ItemRarity.RARE));
            }
            case 5 -> {
                weapons.add(createWarHammer(ItemRarity.EPIC));
                weapons.add(createWarAxe(ItemRarity.LEGENDARY));
                weapons.add(createSpatha(ItemRarity.LEGENDARY));
            }
            default -> weapons.add(createDagger(ItemRarity.COMMON));
        }
        return weapons;
    }

    /**
     * Restituisce una lista di armature appropriate per il tier specificato.
     */
    public static List<Armor> getArmorsForTier(int tier) {
        List<Armor> armors = new ArrayList<>();
        ItemRarity rarity = getRarityForTier(tier);

        switch (tier) {
            case 1 -> armors.add(createLeatherArmor(rarity));
            case 2 -> {
                armors.add(createLeatherArmor(rarity));
                armors.add(createLeatherHelm(rarity));
            }
            case 3 -> {
                armors.add(createChainMail(rarity));
                armors.add(createBronzeHelm(rarity));
                armors.add(createLeatherGreaves(rarity));
            }
            case 4 -> {
                armors.add(createChainMail(ItemRarity.RARE));
                armors.add(createPlateArmor(rarity));
                armors.add(createIronGreaves(rarity));
            }
            case 5 -> {
                armors.add(createPlateArmor(ItemRarity.EPIC));
                armors.add(createBronzeHelm(ItemRarity.LEGENDARY));
                armors.add(createIronGreaves(ItemRarity.LEGENDARY));
            }
            default -> armors.add(createLeatherArmor(ItemRarity.COMMON));
        }
        return armors;
    }

    /**
     * Restituisce una lista di consumabili appropriati per il tier specificato.
     */
    public static List<Consumable> getConsumablesForTier(int tier) {
        List<Consumable> consumables = new ArrayList<>();
        ItemRarity rarity = getRarityForTier(tier);

        consumables.add(createHealthPotion(rarity));
        if (tier >= 2)
            consumables.add(createStrengthElixir(rarity));
        if (tier >= 3)
            consumables.add(createIronSkinPotion(rarity));

        return consumables;
    }

    /**
     * Restituisce tutti gli oggetti disponibili per acquisto in un tier dato.
     */
    public static List<Item> getAllItemsForTier(int tier) {
        List<Item> allItems = new ArrayList<>();
        allItems.addAll(getWeaponsForTier(tier));
        allItems.addAll(getArmorsForTier(tier));
        allItems.addAll(getConsumablesForTier(tier));
        return allItems;
    }

    /**
     * Determina la rarità base degli oggetti per un certo tier.
     */
    private static ItemRarity getRarityForTier(int tier) {
        return switch (tier) {
            case 1 -> ItemRarity.COMMON;
            case 2 -> ItemRarity.UNCOMMON;
            case 3 -> ItemRarity.RARE;
            case 4 -> ItemRarity.EPIC;
            case 5 -> ItemRarity.LEGENDARY;
            default -> ItemRarity.COMMON;
        };
    }
}
