package net.skyblock.core.data;

import lombok.Data;
import net.skyblock.core.stats.StatType;

import java.util.*;

@Data
public class PlayerProfile {

    private final UUID uuid;
    private String playerName;
    private long firstJoin;
    private long lastSeen;
    private boolean profileCreated;

    // ── Stats (combat + gathering) ───────────────────────────
    private final Map<StatType, Double> stats = new EnumMap<>(StatType.class);

    // ── Skills ───────────────────────────────────────────────
    private final Map<String, SkillData> skills = new HashMap<>();

    // ── Collections ──────────────────────────────────────────
    private final Map<String, Long> collections = new HashMap<>();

    // ── Economy ──────────────────────────────────────────────
    private double purse = 0.0;
    private double bankBalance = 0.0;

    // ── Island ────────────────────────────────────────────────
    private String islandId;
    private List<UUID> islandMembers = new ArrayList<>();

    // ── Fairy Souls ───────────────────────────────────────────
    private int fairySouls = 0;

    // ── SkyBlock XP ───────────────────────────────────────────
    private double skyblockXP = 0.0;
    private int skyblockLevel = 0;

    public PlayerProfile(UUID uuid, String playerName) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.firstJoin = System.currentTimeMillis();
        this.lastSeen = System.currentTimeMillis();
        initDefaults();
    }

    private void initDefaults() {
        // Set base stats exactly as Hypixel SkyBlock has them
        for (StatType stat : StatType.values()) {
            stats.put(stat, stat.getBaseValue());
        }

        // Initialize all skills at level 0
        for (String skill : SkillData.SKILL_NAMES) {
            skills.put(skill, new SkillData(skill));
        }
    }

    public double getStat(StatType type) {
        return stats.getOrDefault(type, type.getBaseValue());
    }

    public void setStat(StatType type, double value) {
        stats.put(type, value);
    }

    public void addStat(StatType type, double amount) {
        stats.merge(type, amount, Double::sum);
    }

    public SkillData getSkill(String skillName) {
        return skills.computeIfAbsent(skillName.toLowerCase(), SkillData::new);
    }

    public long getCollection(String itemId) {
        return collections.getOrDefault(itemId.toUpperCase(), 0L);
    }

    public void addCollection(String itemId, long amount) {
        collections.merge(itemId.toUpperCase(), amount, Long::sum);
    }

    /** Effective Health = Health × (Defense / 100 + 1) */
    public double getEffectiveHealth() {
        double hp = getStat(StatType.HEALTH);
        double def = getStat(StatType.DEFENSE);
        return hp * (def / 100.0 + 1.0);
    }

    /** Damage reduction percentage = Defense / (Defense + 100) */
    public double getDamageReduction() {
        double def = getStat(StatType.DEFENSE);
        return def / (def + 100.0);
    }

    public boolean removeCoins(double amount) {
        if (purse < amount) return false;
        purse -= amount;
        return true;
    }
}
