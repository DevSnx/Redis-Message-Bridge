package de.devsnx.redisMessageBridge.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPubSub;
import org.bukkit.Bukkit;

/**
 * @author Marvin Hänel (DevSnx)
 * @since 24.10.2024 22:20
 */

public class ChatManager {

    private final RedisManager redisManager;
    private String channel;

    /**
     * Konstruktor für ChatManager.
     *
     * @param redisManager Die Instanz von RedisManager, die für die Verwaltung der Redis-Verbindung zuständig ist.
     */
    public ChatManager(RedisManager redisManager, @NotNull FileConfiguration config) {
        this.redisManager = redisManager;
        this.channel = config.getString("channel");
        subscribeToChat();
    }

    /**
     * Veröffentlicht eine Chat-Nachricht auf dem globalen Kanal.
     * Diese Nachricht wird an alle Server gesendet, die den Kanal abonniert haben.
     *
     * @param message Die Nachricht, die an alle Server gesendet werden soll.
     */
    public void publishChatMessage(String message) {
        redisManager.publish("globalChat", message);
    }

    /**
     * Abonniert den globalen Chat-Kanal.
     * Wenn eine Nachricht auf diesem Kanal empfangen wird, wird sie an alle Spieler auf dem aktuellen Server gesendet.
     */
    private void subscribeToChat() {
        redisManager.subscribe("globalChat", new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                // Sende die empfangene Nachricht an alle Online-Spieler
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendMessage(message);
                });
            }
        });
    }

}
