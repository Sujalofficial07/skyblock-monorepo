package net.skyblock.core.stats;

import net.kyori.adventure.text.format.TextColor;
import lombok.Getter;

/**
 * Every stat in Hypixel SkyBlock, reproduced exactly.
 * Symbols use the same Unicode characters as the real game.
 */
@Getter
public enum StatType {

    // ── Combat Stats ──────────────────────────────────────────
    HEALTH          ("❤ Health",          "❤",  TextColor.color(0xFF5555), 100.0),
    DEFENSE         ("❈ Defense",         "❈",  TextColor.color(0x55FF55), 0.0),
    TRUE_DEFENSE    ("❂ True Defense",    "❂",  TextColor.color(0xFFFFFF), 0.0),
    STRENGTH        ("❁ Strength",        "❁",  TextColor.color(0xFF5555), 0.0),
    SPEED           ("✦ Speed",           "✦",  TextColor.color(0xFFFFFF), 100.0),
    CRIT_CHANCE     ("☣ Crit Chance",     "☣",  TextColor.color(0xFFAA00), 30.0),
    CRIT_DAMAGE     ("☠ Crit Damage",     "☠",  TextColor.color(0xFFAA00), 50.0),
    INTELLIGENCE    ("✎ Intelligence",    "✎",  TextColor.color(0x55FFFF), 0.0),
    MANA            ("✦ Mana",            "✦",  TextColor.color(0x55FFFF), 100.0),
    FEROCITY        ("⚔ Ferocity",        "⚔",  TextColor.color(0xFF5555), 0.0),
    MAGIC_FIND      ("✧ Magic Find",      "✧",  TextColor.color(0x55FFFF), 0.0),
    BONUS_ATK_SPEED ("⚡ Bonus Atk Spd",  "⚡",  TextColor.color(0xFFFF55), 0.0),
    TRUE_DAMAGE     ("✗ True Damage",     "✗",  TextColor.color(0xFF5555), 0.0),

    // ── Gathering Stats ───────────────────────────────────────
    MINING_SPEED    ("⸕ Mining Speed",    "⸕",  TextColor.color(0x55FFFF), 0.0),
    MINING_FORTUNE  ("☘ Mining Fortune",  "☘",  TextColor.color(0x55FF55), 0.0),
    FARMING_FORTUNE ("☘ Farming Fortune", "☘",  TextColor.color(0x55FF55), 0.0),
    FORAGING_FORTUNE("☘ Foraging Fortune","☘",  TextColor.color(0x55FF55), 0.0),

    // ── Fishing Stats ─────────────────────────────────────────
    SEA_CREATURE_CHANCE("α Sea Creature Chance", "α", TextColor.color(0x55FFFF), 20.0),
    FISHING_SPEED   ("☂ Fishing Speed",   "☂",  TextColor.color(0x5555FF), 0.0),

    // ── Misc ──────────────────────────────────────────────────
    SWING_RANGE     ("⚔ Swing Range",     "⚔",  TextColor.color(0xFFFFFF), 3.0),
    VITALITY        ("♨ Vitality",        "♨",  TextColor.color(0xFF5555), 0.0);

    private final String displayName;
    private final String symbol;
    private final TextColor color;
    private final double baseValue;

    StatType(String displayName, String symbol, TextColor color, double baseValue) {
        this.displayName = displayName;
        this.symbol = symbol;
        this.color = color;
        this.baseValue = baseValue;
    }
}
