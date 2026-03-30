package net.skyblock.core.api;

import net.skyblock.core.data.PlayerProfile;
import net.skyblock.core.stats.StatType;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * The main public API for SkyblockCore.
 * Other plugins obtain this via:
 *   SkyblockAPI api = getServer().getServicesManager()
 *       .load(SkyblockAPI.class);
 */
public interface SkyblockAPI {

    // ── Profile ──────────────────────────────────────────────
    PlayerProfile getProfile(UUID uuid);
    PlayerProfile getProfile(Player player);
    boolean hasProfile(UUID uuid);
    void createProfile(UUID uuid, String name);
    void saveProfile(UUID uuid);

    // ── Stats ─────────────────────────────────────────────────
    double getStat(UUID uuid, StatType stat);
    void setStat(UUID uuid, StatType stat, double value);
    void addStat(UUID uuid, StatType stat, double amount);

    // ── Skills ────────────────────────────────────────────────
    int getSkillLevel(UUID uuid, String skillName);
    double getSkillXP(UUID uuid, String skillName);
    void addSkillXP(UUID uuid, String skillName, double amount);

    // ── Collections ───────────────────────────────────────────
    long getCollectionAmount(UUID uuid, String itemId);
    void addCollection(UUID uuid, String itemId, long amount);

    // ── Economy ───────────────────────────────────────────────
    double getCoins(UUID uuid);
    void addCoins(UUID uuid, double amount);
    boolean removeCoins(UUID uuid, double amount); // returns false if insufficient
}
