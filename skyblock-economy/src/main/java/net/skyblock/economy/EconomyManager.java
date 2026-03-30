package net.skyblock.economy;

import net.skyblock.core.api.SkyblockAPI;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

/**
 * Handles all coin transactions. Mirrors Hypixel SkyBlock economy:
 * - Half your purse is lost on death (without Cookie Buff active)
 * - Bank stores coins safely
 */
public class EconomyManager {

    private final SkyblockAPI api;
    private static final DecimalFormat FORMAT = new DecimalFormat("#,##0.##");

    public EconomyManager(SkyblockAPI api) {
        this.api = api;
    }

    public boolean canAfford(Player player, double amount) {
        return api.getCoins(player.getUniqueId()) >= amount;
    }

    public boolean charge(Player player, double amount) {
        return api.removeCoins(player.getUniqueId(), amount);
    }

    public void pay(Player player, double amount) {
        api.addCoins(player.getUniqueId(), amount);
    }

    public void transferCoins(Player from, Player to, double amount) {
        if (!api.removeCoins(from.getUniqueId(), amount)) {
            from.sendMessage("§cYou don't have enough coins!");
            return;
        }
        api.addCoins(to.getUniqueId(), amount);
        from.sendMessage("§aYou paid §6" + FORMAT.format(amount) + " coins §ato §f"
            + to.getName());
        to.sendMessage("§f" + from.getName() + " §apaid you §6"
            + FORMAT.format(amount) + " coins");
    }

    /** On death: player loses 50% of purse unless they have Cookie Buff */
    public void applyDeathPenalty(Player player) {
        // TODO: check if Cookie Buff is active (from SkyblockItems module)
        double coins = api.getCoins(player.getUniqueId());
        double lost = Math.floor(coins * 0.5);
        if (lost > 0) {
            api.removeCoins(player.getUniqueId(), lost);
            player.sendMessage("§cYou lost §6" + FORMAT.format(lost) + " coins §con your death!");
        }
    }
}
