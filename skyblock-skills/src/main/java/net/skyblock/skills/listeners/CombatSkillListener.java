package net.skyblock.skills.listeners;

import net.skyblock.core.api.SkyblockAPI;
import net.skyblock.core.events.CustomDamageEvent;
import net.skyblock.core.events.SkillLevelUpEvent;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class CombatSkillListener implements Listener {

    private final SkyblockAPI api;

    // XP values per mob type (matching Hypixel SkyBlock approximately)
    private static final java.util.Map<Class<? extends Entity>, Double> MOB_XP = new java.util.HashMap<>();
    static {
        MOB_XP.put(Zombie.class, 5.0);
        MOB_XP.put(Skeleton.class, 5.0);
        MOB_XP.put(Spider.class, 5.0);
        MOB_XP.put(Creeper.class, 5.0);
        MOB_XP.put(Enderman.class, 10.0);
        MOB_XP.put(Blaze.class, 10.0);
        MOB_XP.put(Slime.class, 3.0);
        MOB_XP.put(MagmaCube.class, 5.0);
        MOB_XP.put(Witch.class, 12.0);
        MOB_XP.put(WitherSkeleton.class, 15.0);
        MOB_XP.put(Ghast.class, 15.0);
    }

    public CombatSkillListener(SkyblockAPI api) {
        this.api = api;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;

        // Look up XP for this mob type
        double xpGain = 0;
        for (Map.Entry<Class<? extends Entity>, Double> entry : MOB_XP.entrySet()) {
            if (entry.getKey().isAssignableFrom(e.getEntity().getClass())) {
                xpGain = entry.getValue();
                break;
            }
        }

        if (xpGain <= 0) return;

        // Check for skill level up
        int levelBefore = api.getSkillLevel(killer.getUniqueId(), "combat");
        api.addSkillXP(killer.getUniqueId(), "combat", xpGain);
        int levelAfter = api.getSkillLevel(killer.getUniqueId(), "combat");

        if (levelAfter > levelBefore) {
            // Fire level up event for other plugins to listen to
            SkillLevelUpEvent event = new SkillLevelUpEvent(
                killer, "combat", levelBefore, levelAfter);
            killer.getServer().getPluginManager().callEvent(event);
        }
    }
}
