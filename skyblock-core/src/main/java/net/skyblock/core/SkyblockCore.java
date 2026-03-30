package net.skyblock.core;

import net.skyblock.core.api.SkyblockAPI;
import net.skyblock.core.data.DatabaseManager;
import net.skyblock.core.data.ProfileCache;
import net.skyblock.core.listeners.PlayerJoinQuitListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkyblockCore extends JavaPlugin {

    private static SkyblockCore instance;
    private DatabaseManager databaseManager;
    private ProfileCache profileCache;
    private SkyblockAPI api;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        // Initialize storage
        this.databaseManager = new DatabaseManager(this);
        this.databaseManager.init();

        // Initialize cache
        this.profileCache = new ProfileCache(this, databaseManager);

        // Register API with Bukkit's service manager so other plugins can find it
        this.api = new SkyblockAPIImpl(this);
        getServer().getServicesManager().register(
            SkyblockAPI.class, api, this, org.bukkit.plugin.ServicePriority.Normal
        );

        // Register listeners
        getServer().getPluginManager().registerEvents(
            new PlayerJoinQuitListener(this), this
        );

        // Register commands
        getCommand("skyblock").setExecutor(new SkyblockCommand(this));
        getCommand("sbadmin").setExecutor(new SkyblockAdminCommand(this));

        getLogger().info("SkyblockCore enabled! Loaded " 
            + profileCache.getCachedCount() + " profiles.");
    }

    @Override
    public void onDisable() {
        // Flush all cached profiles to disk/DB on shutdown
        if (profileCache != null) {
            profileCache.flushAll();
        }
        if (databaseManager != null) {
            databaseManager.close();
        }
        getLogger().info("SkyblockCore disabled. All profiles saved.");
    }

    public static SkyblockCore getInstance() { return instance; }
    public DatabaseManager getDatabaseManager() { return databaseManager; }
    public ProfileCache getProfileCache() { return profileCache; }
    public SkyblockAPI getAPI() { return api; }
}
