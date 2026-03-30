package net.skyblock.core.data;

import lombok.Data;
import java.util.Arrays;
import java.util.List;

@Data
public class SkillData {

    public static final List<String> SKILL_NAMES = Arrays.asList(
        "farming", "mining", "combat", "foraging", "fishing",
        "enchanting", "alchemy", "taming", "carpentry",
        "runecrafting", "dungeoneering"
    );

    /** Hypixel's actual XP table for skill levels 1-60
     *  These are cumulative XP required for each level. */
    private static final long[] XP_TABLE = {
        0, 50, 175, 375, 675, 1175, 1925, 2925, 4425,
        6425, 9925, 14925, 22425, 32425, 47425, 67425,
        97425, 147425, 222425, 322425, 522425, 822425,
        1222425, 1722425, 2322425, 3022425, 3822425,
        4722425, 5722425, 6822425, 8022425, 9322425,
        10722425, 12222425, 13822425, 15522425, 17322425,
        19222425, 21222425, 23322425, 25522425, 27822425,
        30222425, 32722425, 35322425, 38072425, 40972425,
        44072425, 47472425, 51172425,
        // Levels 51-60 (Farming/Mining/Combat/Enchanting only)
        55172425, 59472425, 64072425, 68972425, 74172425,
        79672425, 85472425, 91572425, 97972425, 104672425
    };

    private final String name;
    private int level;
    private double currentXP;
    private double totalXP;

    public SkillData(String name) {
        this.name = name;
        this.level = 0;
        this.currentXP = 0;
        this.totalXP = 0;
    }

    public boolean addXP(double amount) {
        this.currentXP += amount;
        this.totalXP += amount;
        return checkLevelUp();
    }

    private boolean checkLevelUp() {
        int maxLevel = isHighCapSkill() ? 60 : 50;
        boolean leveled = false;
        while (level < maxLevel && totalXP >= XP_TABLE[level + 1]) {
            level++;
            leveled = true;
        }
        return leveled;
    }

    public double getXPForNextLevel() {
        int maxLevel = isHighCapSkill() ? 60 : 50;
        if (level >= maxLevel) return -1;
        return XP_TABLE[level + 1] - totalXP;
    }

    public double getLevelProgress() {
        int maxLevel = isHighCapSkill() ? 60 : 50;
        if (level >= maxLevel) return 1.0;
        long needed = XP_TABLE[level + 1] - (level > 0 ? XP_TABLE[level] : 0);
        long have = (long)(totalXP - (level > 0 ? XP_TABLE[level] : 0));
        return Math.max(0, Math.min(1.0, (double) have / needed));
    }

    private boolean isHighCapSkill() {
        return name.equals("farming") || name.equals("mining")
            || name.equals("combat") || name.equals("enchanting");
    }
}
