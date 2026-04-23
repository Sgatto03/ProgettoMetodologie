package it.unicam.cs.mpgc.rpg123297.model.character;

import it.unicam.cs.mpgc.rpg123297.model.combat.ai.AggressiveAI;
import it.unicam.cs.mpgc.rpg123297.model.combat.ai.BalancedAI;
import it.unicam.cs.mpgc.rpg123297.model.combat.ai.DefensiveAI;
import it.unicam.cs.mpgc.rpg123297.model.combat.ai.EnemyAI;
import it.unicam.cs.mpgc.rpg123297.model.item.ItemFactory;
import it.unicam.cs.mpgc.rpg123297.model.item.Weapon;
import it.unicam.cs.mpgc.rpg123297.model.item.Armor;
import it.unicam.cs.mpgc.rpg123297.model.item.EquipmentSlot;

import java.util.List;
import java.util.Random;

/**
 * Factory per la creazione di nemici in base al tier dell'arena.
 * Utilizza il Factory Pattern per centralizzare la logica di creazione
 * e rendere facile l'aggiunta di nuovi tipi di nemici.
 */
public class EnemyFactory {

    private static final Random RANDOM = new Random();

    /** Dati dei nemici per ogni tier: [nome, descrizione] */
    private static final String[][] TIER_1_ENEMIES = {
            { "Bandito", "Un criminale disperato armato di coltello." },
            { "Schiavo Ribelle", "Uno schiavo fuggito, combatte per la libertà." },
            { "Teppista", "Un teppista di strada che cerca guai." }
    };

    private static final String[][] TIER_2_ENEMIES = {
            { "Gladiatore Novizio", "Un gladiatore alle prime armi." },
            { "Mercenario", "Un soldato di ventura esperto di lame." },
            { "Combattente Tribale", "Un guerriero barbaro feroce." }
    };

    private static final String[][] TIER_3_ENEMIES = {
            { "Gladiatore Veterano", "Un combattente esperto dell'arena." },
            { "Centurione Caduto", "Un ex ufficiale romano degradato." },
            { "Campione Provinciale", "Il miglior combattente della provincia." }
    };

    private static final String[][] TIER_4_ENEMIES = {
            { "Campione del Colosseo", "Un gladiatore leggendario." },
            { "Bestia dell'Arena", "Una fiera addestrata per uccidere." },
            { "Pretoriano Rinnegato", "Un ex guardia imperiale temibile." }
    };

    private static final String[][] TIER_5_ENEMIES = {
            { "Semidio della Guerra", "Un guerriero benedetto da Marte." },
            { "Titano dell'Arena", "Un colosso imbattuto da anni." },
            { "Ombra di Spartaco", "Lo spirito del più grande gladiatore." }
    };

    private EnemyFactory() {
        // impedisce l'istanziazione diretta — solo metodi statici
    }

    /**
     * Crea un nemico casuale per il tier specificato.
     * 
     * @param tier livello dell'arena (1-5)
     * @return un nuovo nemico con statistiche e equipaggiamento adeguati al tier
     */
    public static Enemy createRandomEnemy(int tier) {
        String[][] enemyData = getEnemyDataForTier(tier);
        int index = RANDOM.nextInt(enemyData.length);
        String name = enemyData[index][0];
        String description = enemyData[index][1];

        CharacterStats stats = generateStatsForTier(tier);
        EnemyAI ai = selectAIForTier(tier);
        int[] rewards = calculateRewards(tier);

        Enemy enemy = new Enemy(name, stats, tier, description,
                rewards[0], rewards[1], rewards[2], ai);

        // Equipaggia il nemico con armi e armature appropriate al tier
        equipEnemyForTier(enemy, tier);

        return enemy;
    }

    /**
     * Crea un nemico specifico per nome e tier (utile per sfide boss).
     */
    public static Enemy createNamedEnemy(String name, String description,
            int tier, EnemyAI ai) {
        CharacterStats stats = generateStatsForTier(tier);
        int[] rewards = calculateRewards(tier);
        // Boss hanno ricompense doppie
        Enemy enemy = new Enemy(name, stats, tier, description,
                rewards[0] * 2, rewards[1] * 2, rewards[2] * 2, ai);
        equipEnemyForTier(enemy, tier);
        return enemy;
    }

    private static String[][] getEnemyDataForTier(int tier) {
        return switch (tier) {
            case 1 -> TIER_1_ENEMIES;
            case 2 -> TIER_2_ENEMIES;
            case 3 -> TIER_3_ENEMIES;
            case 4 -> TIER_4_ENEMIES;
            case 5 -> TIER_5_ENEMIES;
            default -> throw new IllegalArgumentException("Tier non valido: " + tier);
        };
    }

    private static CharacterStats generateStatsForTier(int tier) {
        int base = 2 + (tier * 2);
        int variance = RANDOM.nextInt(3) - 1; // -1, 0, +1
        return new CharacterStats(
                base + RANDOM.nextInt(3) + variance, // STR
                base + RANDOM.nextInt(3) + variance, // AGI
                base + RANDOM.nextInt(3) + variance, // END
                base + RANDOM.nextInt(3) + variance, // DEX
                base - 1 // CHA (nemici hanno meno carisma)
        );
    }

    private static EnemyAI selectAIForTier(int tier) {
        // Tier più alti hanno AI più sofisticate
        if (tier <= 2) {
            return RANDOM.nextBoolean() ? new AggressiveAI() : new BalancedAI();
        } else if (tier <= 4) {
            return switch (RANDOM.nextInt(3)) {
                case 0 -> new AggressiveAI();
                case 1 -> new DefensiveAI();
                default -> new BalancedAI();
            };
        } else {
            // Tier 5: prevalentemente bilanciati (più intelligenti)
            return RANDOM.nextInt(3) == 0 ? new AggressiveAI() : new BalancedAI();
        }
    }

    private static int[] calculateRewards(int tier) {
        int baseGold = 10 + (tier * 15);
        int baseXp = 20 + (tier * 25);
        int baseFame = 10 + (tier * 15);
        int variance = RANDOM.nextInt(10);
        return new int[] { baseGold + variance, baseXp + variance, baseFame + variance };
    }

    private static void equipEnemyForTier(Enemy enemy, int tier) {
        List<Weapon> weapons = ItemFactory.getWeaponsForTier(tier);
        List<Armor> armors = ItemFactory.getArmorsForTier(tier);
        if (!weapons.isEmpty()) {
            enemy.getEquipment().equip(EquipmentSlot.WEAPON,
                    weapons.get(RANDOM.nextInt(weapons.size())));
        }
        if (!armors.isEmpty()) {
            enemy.getEquipment().equip(EquipmentSlot.CHEST,
                    armors.get(RANDOM.nextInt(armors.size())));
        }
    }
}
