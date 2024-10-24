package de.devsnx.redisMessageBridge;

import de.devsnx.redisMessageBridge.listener.PlayerChatListener;
import de.devsnx.redisMessageBridge.manager.ChatManager;
import de.devsnx.redisMessageBridge.manager.RedisManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedisMessageBridge extends JavaPlugin {

    public static RedisMessageBridge instance;
    private RedisManager redisManager;
    private ChatManager chatManager;

    @Override
    public void onEnable() {
        instance = this;

        // Konfigurationsdatei laden/generieren
        saveDefaultConfig();

        String redisHost = getConfig().getString("redis.host", "localhost");
        int redisPort = getConfig().getInt("redis.port", 6379);
        String redisUsername = getConfig().getString("redis.host", "redisuser");
        String redisPassword = getConfig().getString("redis.host", "redispassword");

        redisManager = new RedisManager(getConfig());
        chatManager = new ChatManager(redisManager, getConfig());

        // Event-Listener registrieren
        getServer().getPluginManager().registerEvents(new PlayerChatListener(chatManager), this);

        getLogger().info("Redis Inventory Bridge aktiviert!");

    }

    @Override
    public void onDisable() {
        // Plugin wird deaktiviert - schließe Redis-Verbindung
        if (redisManager != null) {
            redisManager.close();
            getLogger().info("Redis-Verbindung geschlossen.");
        }

        getLogger().info("Redis Inventory Bridge deaktiviert!");

        instance = null;
    }

    public static RedisMessageBridge getInstance() {
        return instance;
    }

}