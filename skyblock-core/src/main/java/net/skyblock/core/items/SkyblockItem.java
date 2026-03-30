package net.skyblock.core.items;

import lombok.Builder;
import lombok.Data;
import net.skyblock.core.stats.StatType;

import java.util.*;

/**
 * Represents a custom SkyBlock item with all Hypixel-style metadata.
 */
@Data
@Builder
public class SkyblockItem {

    private final String itemId;          // e.g. "ASPECT_OF_THE_END"
    private final String displayName;     // Colored display name
    private final ItemRarity rarity;
    private final ItemType type;          // SWORD, BOW, HELMET, etc.

    // Stats this item grants when held/worn
    @Builder.Default
    private Map<StatType, Double> stats = new EnumMap<>(StatType.class);

    // Lore lines (before stats section)
    @Builder.Default
    private List<String> loreLines = new ArrayList<>();

    // Abilities (right-click, passive, etc.)
    @Builder.Default
    private List<ItemAbility> abilities = new ArrayList<>();

    // Is this item soul-bound? (cannot be traded)
    @Builder.Default
    private boolean soulbound = false;

    // Is this item stackable as a custom item?
    @Builder.Default
    private boolean stackable = false;

    // Required collection to craft
    private String requiredCollection;
    private int requiredCollectionTier;

    // NPC sell value
    @Builder.Default
    private double npcValue = 0;

    public enum ItemType {
        SWORD, BOW, WAND, FISHING_ROD,
        HELMET, CHESTPLATE, LEGGINGS, BOOTS,
        ACCESSORY, CONSUMABLE, MATERIAL, MINION_ITEM
    }
}
