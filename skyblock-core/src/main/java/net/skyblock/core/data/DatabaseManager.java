package net.skyblock.core.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.google.gson.Gson;
import net.skyblock.core.SkyblockCore;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.sql.*;
import java.util.UUID;

public class DatabaseManager {

    private final SkyblockCore plugin;
    private HikariDataSource dataSource;
    private final Gson gson = new Gson();

    public DatabaseManager(SkyblockCore plugin) {
        this.plugin = plugin;
    }

    public void init() {
        FileConfiguration config = plugin.getConfig();
        String type = config.getString("storage.type", "sqlite");

        HikariConfig hikari = new HikariConfig();

        if (type.equalsIgnoreCase("mysql")) {
            String host = config.getString("storage.mysql.host", "localhost");
            int port = config.getInt("storage.mysql.port", 3306);
            String db = config.getString("storage.mysql.database", "skyblock");
            String user = config.getString("storage.mysql.username", "root");
            String pass = config.getString("storage.mysql.password", "");
            int poolSize = config.getInt("storage.mysql.pool-size", 10);

            hikari.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + db
                + "?useSSL=false&autoReconnect=true");
            hikari.setUsername(user);
            hikari.setPassword(pass);
            hikari.setMaximumPoolSize(poolSize);
            hikari.setDriverClassName("com.mysql.cj.jdbc.Driver");
        } else {
            File dbFile = new File(plugin.getDataFolder(), "skyblock_data.db");
            hikari.setJdbcUrl("jdbc:sqlite:" + dbFile.getAbsolutePath());
            hikari.setMaximumPoolSize(4);
            hikari.setDriverClassName("org.sqlite.JDBC");
        }

        hikari.setPoolName("SkyblockCore-Pool");
        hikari.setConnectionTimeout(30000);
        hikari.setIdleTimeout(600000);
        hikari.setMaxLifetime(1800000);
        this.dataSource = new HikariDataSource(hikari);

        createTables();
        plugin.getLogger().info("[DB] Storage initialized (" + type + ")");
    }

    private void createTables() {
        String sql = """
            CREATE TABLE IF NOT EXISTS skyblock_profiles (
                uuid        TEXT PRIMARY KEY,
                name        TEXT NOT NULL,
                data        TEXT NOT NULL,
                created_at  INTEGER NOT NULL,
                updated_at  INTEGER NOT NULL
            )
            """;
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            plugin.getLogger().severe("[DB] Table creation failed: " + e.getMessage());
        }
    }

    public PlayerProfile loadProfile(UUID uuid) {
        String sql = "SELECT data FROM skyblock_profiles WHERE uuid = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String json = rs.getString("data");
                    return gson.fromJson(json, PlayerProfile.class);
                }
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("[DB] Load failed for " + uuid + ": " + e.getMessage());
        }
        return null;
    }

    public void saveProfile(PlayerProfile profile) {
        String sql = """
            INSERT INTO skyblock_profiles (uuid, name, data, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT(uuid) DO UPDATE SET
                name = excluded.name,
                data = excluded.data,
                updated_at = excluded.updated_at
            """;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, profile.getUuid().toString());
            ps.setString(2, profile.getPlayerName());
            ps.setString(3, gson.toJson(profile));
            ps.setLong(4, profile.getFirstJoin());
            ps.setLong(5, System.currentTimeMillis());
            ps.executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("[DB] Save failed for "
                + profile.getUuid() + ": " + e.getMessage());
        }
    }

    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
