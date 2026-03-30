package net.skyblock.skills;

import net.skyblock.core.api.SkyblockAPI;
import net.skyblock.skills.listeners.*;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyblockSkills extends JavaPlugin {

    private SkyblockAPI coreAPI;

    @Override
    public void onEnable() {
        RegisteredServiceProvider<SkyblockAPI> rsp =
            getServer().getServicesManager().getRegistration(SkyblockAPI.class);

        if (rsp == null) {
            getLogger().severe("SkyblockCore not found! Disabling SkyblockSkills.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.coreAPI = rsp.getProvider();

        // Register skill XP listeners
        getServer().getPluginManager().registerEvents(
            new FarmingSkillListener(coreAPI), this);
        getServer().getPluginManager().registerEvents(
            new MiningSkillListener(coreAPI), this);
        getServer().getPluginManager().registerEvents(
            new CombatSkillListener(coreAPI), this);
        getServer().getPluginManager().registerEvents(
            new ForagingSkillListener(coreAPI), this);
        getServer().getPluginManager().registerEvents(
            new FishingSkillListener(coreAPI), this);

        getLogger().info("SkyblockSkills enabled.");
    }
}
