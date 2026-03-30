package net.skyblock.combat.listeners;

import net.skyblock.core.SkyblockCore;
import net.skyblock.core.api.SkyblockAPI;
import net.skyblock.core.data.PlayerProfile;
import net.skyblock.core.events.CustomDamageEvent;
import net.skyblock.core.items.NBTHandler;
import net.skyblock.core.stats.StatType;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class CombatListener implements Listener {

    private final SkyblockAPI api;
    private final SkyblockCore core;

    public CombatListener(SkyblockAPI api, SkyblockCore core) {
        this.api = api;
        this.core = core;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        Entity damager = e.getDamager();
        if (!(damager instanceof Player player)) return;

        // Cancel vanilla damage — we handle it ourselves
        e.setCancelled(true);

        PlayerProfile profile = api.getProfile(player.getUniqueId());
        if (profile == null) return;

        // Get weapon damage from held item
        ItemStack held = player.getInventory().getItemInMainHand();
        int weaponDamage = getWeaponDamage(held);

        // Fire our custom event
        CustomDamageEvent event = new CustomDamageEvent(
            player, e.getEntity(), profile, weaponDamage,
            CustomDamageEvent.DamageType.MELEE
        );
        player.getServer().getPluginManager().callEvent(event);

        if (event.isCancelled()) return;

        // Apply damage to victim
        Entity victim = event.getVictim();
        if (victim instanceof LivingEntity living) {
            living.damage(event.getFinalDamage(), player);

            // Show floating damage indicator (action-bar style)
            showDamageIndicator(player, event);
        }
    }

    private int getWeaponDamage(ItemStack item) {
        if (item == null || item.getType().isAir()) return 0;
        // Check PDC for custom weapon damage first
        // Then fall back to vanilla material damage values
        return switch (item.getType()) {
            case WOODEN_SWORD  -> 4;
            case STONE_SWORD   -> 5;
            case IRON_SWORD    -> 6;
            case GOLDEN_SWORD  -> 4;
            case DIAMOND_SWORD -> 7;
            case NETHERITE_SWORD -> 8;
            default -> 1;
        };
    }

    private void showDamageIndicator(Player player, CustomDamageEvent event) {
        // Send damage number as a hologram/chat message
        // In production: use packets or a hologram library
        String indicator = event.isCrit()
            ? "§c✧" + (int) event.getFinalDamage() + "✧"
            : "§7" + (int) event.getFinalDamage();
        player.sendActionBar(net.kyori.adventure.text.Component.text(indicator));
    }
}
