package net.skyblock.ui.scoreboard;

import net.skyblock.core.api.SkyblockAPI;
import net.skyblock.core.data.PlayerProfile;
import net.skyblock.core.stats.StatType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.text.DecimalFormat;

/**
 * Recreates Hypixel SkyBlock's sidebar scoreboard exactly:
 *
 * ♕ SKYBLOCK
 *  --------
 *  ❤ Health: 1,420/1,420
 *  ❈ Defense: 350
 *  ✦ Mana: 540/540
 *  --------
 *  ⛂ Combat: 32
 *  --------
 *  Coins: 1,234,567
 */
public class SkyblockScoreboard {

    private static final DecimalFormat FORMAT = new DecimalFormat("#,###");
    private final SkyblockAPI api;

    public SkyblockScoreboard(SkyblockAPI api) {
        this.api = api;
    }

    public void update(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();

        Objective obj = board.registerNewObjective(
            "skyblock", Criteria.DUMMY,
            net.kyori.adventure.text.Component.text("§6§lSKYBLOCK")
        );
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        PlayerProfile profile = api.getProfile(player.getUniqueId());
        if (profile == null) return;

        int line = 15;

        // Day counter (stub)
        score(obj, "§7" + formatDate(), line--);
        score(obj, "§8§m----------", line--);

        // Health
        double hp = profile.getStat(StatType.HEALTH);
        double maxHp = hp; // In full game: max hp from items
        score(obj, "§c❤ " + FORMAT.format((int) hp)
            + "§7/§c" + FORMAT.format((int) maxHp), line--);

        // Defense
        double def = profile.getStat(StatType.DEFENSE);
        score(obj, "§a❈ Defense: §f" + FORMAT.format((int) def), line--);

        // Mana
        double intel = profile.getStat(StatType.INTELLIGENCE);
        double maxMana = intel + 100;
        score(obj, "§b✦ " + FORMAT.format((int) maxMana)
            + "§7/§b" + FORMAT.format((int) maxMana), line--);

        score(obj, "§8§m----------", line--);

        // Skill levels
        score(obj, "⚔ Combat " + api.getSkillLevel(player.getUniqueId(), "combat"),
            line--);
        score(obj, "⛏ Mining " + api.getSkillLevel(player.getUniqueId(), "mining"),
            line--);

        score(obj, "§8§m----------", line--);

        // Coins
        double coins = api.getCoins(player.getUniqueId());
        score(obj, "§6Coins: §f" + FORMAT.format((long) coins), line--);

        player.setScoreboard(board);
    }

    private void score(Objective obj, String text, int value) {
        Score s = obj.getScore(text);
        s.setScore(value);
    }

    private String formatDate() {
        // Stub — in full game: track SkyBlock calendar
        return "Early Summer §e2nd";
    }
}
