package net.skyblock.collections.registry;

import lombok.Data;
import java.util.*;

/**
 * Defines all SkyBlock collections with exact tier thresholds.
 * Mirrors Hypixel SkyBlock's collection system.
 */
public class CollectionRegistry {

    private static final Map<String, CollectionDefinition> REGISTRY = new LinkedHashMap<>();

    static {
        // ── FARMING ──────────────────────────────────────────────
        register("WHEAT",       Category.FARMING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("CARROT",      Category.FARMING,
            new long[]{100,250,500,1000,2500,5000,10000,25000,50000,100000});
        register("POTATO",      Category.FARMING,
            new long[]{100,250,500,1000,2500,5000,10000,25000,50000,100000});
        register("PUMPKIN",     Category.FARMING,
            new long[]{40,100,250,1000,5000,25000,50000,100000,250000,500000});
        register("MELON",       Category.FARMING,
            new long[]{250,500,1000,2500,5000,10000,25000,50000,100000,250000});
        register("MUSHROOM",    Category.FARMING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("CACTUS",      Category.FARMING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("SUGAR_CANE",  Category.FARMING,
            new long[]{50,100,250,500,2500,25000,50000,100000,250000,500000});
        register("LEATHER",     Category.FARMING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});

        // ── MINING ───────────────────────────────────────────────
        register("COBBLESTONE",  Category.MINING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("COAL",         Category.MINING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("IRON_INGOT",   Category.MINING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("GOLD_INGOT",   Category.MINING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("DIAMOND",      Category.MINING,
            new long[]{50,100,250,1000,5000,10000,25000,50000,100000,250000});
        register("LAPIS_LAZULI", Category.MINING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("EMERALD",      Category.MINING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("REDSTONE",     Category.MINING,
            new long[]{50,100,250,500,1000,2500,10000,25000,100000,200000});
        register("OBSIDIAN",     Category.MINING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("GLOWSTONE",    Category.MINING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("GRAVEL",       Category.MINING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});

        // ── COMBAT ────────────────────────────────────────────────
        register("ROTTEN_FLESH",  Category.COMBAT,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("BONE",          Category.COMBAT,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("STRING",        Category.COMBAT,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("GUNPOWDER",     Category.COMBAT,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("ENDER_PEARL",   Category.COMBAT,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("BLAZE_ROD",     Category.COMBAT,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});

        // ── FORAGING ──────────────────────────────────────────────
        register("OAK_LOG",     Category.FORAGING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("BIRCH_LOG",   Category.FORAGING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("SPRUCE_LOG",  Category.FORAGING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("JUNGLE_LOG",  Category.FORAGING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("ACACIA_LOG",  Category.FORAGING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});
        register("DARK_OAK_LOG",Category.FORAGING,
            new long[]{50,100,250,500,1000,2500,5000,10000,25000,50000});

        // ── FISHING ───────────────────────────────────────────────
        register("RAW_FISH",    Category.FISHING,
            new long[]{20,50,100,250,500,1000,2500,5000,10000,30000});
        register("RAW_SALMON",  Category.FISHING,
            new long[]{20,50,100,250,500,1000,2500,5000,10000,30000});
        register("CLOWNFISH",   Category.FISHING,
            new long[]{20,50,100,250,500,1000,2500,5000,10000,30000});
        register("PUFFERFISH",  Category.FISHING,
            new long[]{20,50,100,250,500,1000,2500,5000,10000,30000});
    }

    private static void register(String id, Category cat, long[] tiers) {
        REGISTRY.put(id, new CollectionDefinition(id, cat, tiers));
    }

    public static CollectionDefinition get(String id) {
        return REGISTRY.get(id.toUpperCase());
    }

    public static Collection<CollectionDefinition> getAll() {
        return REGISTRY.values();
    }

    public static Collection<CollectionDefinition> getByCategory(Category cat) {
        List<CollectionDefinition> result = new ArrayList<>();
        for (CollectionDefinition def : REGISTRY.values()) {
            if (def.getCategory() == cat) result.add(def);
        }
        return result;
    }

    @Data
    public static class CollectionDefinition {
        private final String id;
        private final Category category;
        private final long[] tierThresholds; // length = number of tiers

        public int getTierForAmount(long amount) {
            int tier = 0;
            for (long threshold : tierThresholds) {
                if (amount >= threshold) tier++;
                else break;
            }
            return tier;
        }

        public long getNextThreshold(long currentAmount) {
            for (long threshold : tierThresholds) {
                if (currentAmount < threshold) return threshold;
            }
            return -1; // maxed
        }
    }

    public enum Category {
        FARMING, MINING, COMBAT, FORAGING, FISHING
    }
}
