package net.skyblock.core.items;

import net.skyblock.core.SkyblockCore;
import net.skyblock.core.stats.StatType;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.EnumMap;
import java.util.Map;

/**
 * Handles reading and writing custom SkyBlock data from/to ItemStacks
 * using Bukkit's PersistentDataContainer (PDC) — the correct approach.
 * No lore parsing!
 */
public class NBTHandler {

    // Keys stored in PDC
    private static NamespacedKey KEY_ITEM_ID;
    private static NamespacedKey KEY_RARITY;
    private static NamespacedKey KEY_TYPE;
    private static NamespacedKey KEY_SOULBOUND;
    private static NamespacedKey KEY_STAT_PREFIX;  // We store each stat as "stat_HEALTH" etc.

    public static void init(SkyblockCore plugin) {
        KEY_ITEM_ID    = new NamespacedKey(plugin, "item_id");
        KEY_RARITY     = new NamespacedKey(plugin, "rarity");
        KEY_TYPE       = new NamespacedKey(plugin, "item_type");
        KEY_SOULBOUND  = new NamespacedKey(plugin, "soulbound");
    }

    // ── Write ────────────────────────────────────────────────

    public static void setItemId(ItemStack item, String id) {
        item.editMeta(meta -> meta.getPersistentDataContainer()
            .set(KEY_ITEM_ID, PersistentDataType.STRING, id));
    }

    public static void setRarity(ItemStack item, ItemRarity rarity) {
        item.editMeta(meta -> meta.getPersistentDataContainer()
            .set(KEY_RARITY, PersistentDataType.STRING, rarity.name()));
    }

    public static void setItemType(ItemStack item, SkyblockItem.ItemType type) {
        item.editMeta(meta -> meta.getPersistentDataContainer()
            .set(KEY_TYPE, PersistentDataType.STRING, type.name()));
    }

    public static void setSoulbound(ItemStack item, boolean soulbound) {
        item.editMeta(meta -> meta.getPersistentDataContainer()
            .set(KEY_SOULBOUND, PersistentDataType.BOOLEAN, soulbound));
    }

    public static void setStat(SkyblockCore plugin, ItemStack item,
                               StatType stat, double value) {
        NamespacedKey key = new NamespacedKey(plugin, "stat_" + stat.name().toLowerCase());
        item.editMeta(meta -> meta.getPersistentDataContainer()
            .set(key, PersistentDataType.DOUBLE, value));
    }

    // ── Read ─────────────────────────────────────────────────

    public static String getItemId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        return pdc.get(KEY_ITEM_ID, PersistentDataType.STRING);
    }

    public static ItemRarity getRarity(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        String rarityStr = pdc.get(KEY_RARITY, PersistentDataType.STRING);
        if (rarityStr == null) return null;
        try { return ItemRarity.valueOf(rarityStr); }
        catch (IllegalArgumentException e) { return null; }
    }

    public static boolean isSkyblockItem(ItemStack item) {
        return getItemId(item) != null;
    }

    public static boolean isSoulbound(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        Boolean val = pdc.get(KEY_SOULBOUND, PersistentDataType.BOOLEAN);
        return val != null && val;
    }

    public static Map<StatType, Double> getStats(SkyblockCore plugin, ItemStack item) {
        Map<StatType, Double> result = new EnumMap<>(StatType.class);
        if (item == null || !item.hasItemMeta()) return result;
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        for (StatType stat : StatType.values()) {
            NamespacedKey key = new NamespacedKey(plugin,
                "stat_" + stat.name().toLowerCase());
            Double val = pdc.get(key, PersistentDataType.DOUBLE);
            if (val != null) result.put(stat, val);
        }
        return result;
    }
}
