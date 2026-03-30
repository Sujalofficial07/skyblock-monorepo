package net.skyblock.core.items;

import lombok.Getter;
import net.kyori.adventure.text.format.TextColor;

/**
 * Hypixel SkyBlock item rarities, in order from worst to best.
 * Colors exactly match the real game.
 */
@Getter
public enum ItemRarity {

    COMMON      ("COMMON",       TextColor.color(0xFFFFFF), "§f",  1),
    UNCOMMON    ("UNCOMMON",     TextColor.color(0x55FF55), "§a",  2),
    RARE        ("RARE",         TextColor.color(0x5555FF), "§9",  3),
    EPIC        ("EPIC",         TextColor.color(0xAA00AA), "§5",  4),
    LEGENDARY   ("LEGENDARY",    TextColor.color(0xFFAA00), "§6",  5),
    MYTHIC      ("MYTHIC",       TextColor.color(0xFF55FF), "§d",  6),
    DIVINE      ("DIVINE",       TextColor.color(0x55FFFF), "§b",  7),
    SPECIAL     ("SPECIAL",      TextColor.color(0xFF5555), "§c",  8),
    VERY_SPECIAL("VERY SPECIAL", TextColor.color(0xFF5555), "§c",  9);

    private final String displayName;
    private final TextColor color;
    private final String legacyColor; // For item lore formatting
    private final int tier;

    ItemRarity(String displayName, TextColor color, String legacyColor, int tier) {
        this.displayName = displayName;
        this.color = color;
        this.legacyColor = legacyColor;
        this.tier = tier;
    }

    public ItemRarity upgrade() {
        ItemRarity[] values = values();
        int next = Math.min(ordinal() + 1, values.length - 1);
        return values[next];
    }
}
